package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Motor de IA para recomendación de nivel de dificultad.
 *
 * CA-01: Reglas de tendencia con umbral de confianza ≥ 0.70.
 *   MEJORA  (confianza ≥ 0.70, nivel < máximo) → sube un nivel.
 *   REGRESION (confianza ≥ 0.70, nivel > mínimo) → baja un nivel.
 *   ESTANCAMIENTO → mantiene nivel actual.
 *
 * CA-02: Guarda cada recomendación en ia_recomendacion.
 * CA-03: Requiere mínimo 3 sesiones completadas; sin ellas no genera nada.
 * CA-05: Registra los IDs de sesiones que originaron la recomendación en JSON.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IaRecomendacionService {

    // ── Constantes del algoritmo ──────────────────────────────────────────
    private static final int    SESIONES_MINIMAS  = 3;
    private static final int    SESIONES_VENTANA  = 5;
    private static final double CONFIANZA_MINIMA  = 0.70;

    // ── Repositorios ──────────────────────────────────────────────────────
    private final IaRecomendacionRepository recomendacionRepository;
    private final SesionJuegoRepository     sesionJuegoRepository;
    private final NivelDificultadRepository nivelDificultadRepository;

    // ═══════════════════════════════════════════════════════════════════════
    // API pública
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Devuelve la recomendación vigente (la más reciente) para un niño + juego.
     * Si aún no existe ninguna, devuelve Optional.empty().
     */
    public Optional<IaRecomendacion> obtenerRecomendacion(Integer perfilId, Integer juegoId) {
        return recomendacionRepository
                .findTopByPerfilIdAndJuegoIdOrderByFechaRecomendacionDesc(perfilId, juegoId);
    }

    /**
     * Historial completo de recomendaciones para un niño + juego (más reciente primero).
     */
    public List<IaRecomendacion> obtenerHistorial(Integer perfilId, Integer juegoId) {
        return recomendacionRepository
                .findByPerfilIdAndJuegoIdOrderByFechaRecomendacionDesc(perfilId, juegoId);
    }

    /**
     * Analiza las últimas sesiones de un niño en un juego y persiste una nueva
     * recomendación. Se llama automáticamente al finalizar cada sesión.
     *
     * CA-03: Si hay menos de {@value #SESIONES_MINIMAS} sesiones completadas,
     * no genera recomendación y devuelve null.
     */
    @Transactional
    public IaRecomendacion generarRecomendacion(Integer perfilId, Integer juegoId) {
        // 1. Cargar sesiones completadas con métricas disponibles
        List<SesionJuego> sesiones = sesionJuegoRepository
                .findByPerfilIdAndJuegoId(perfilId, juegoId)
                .stream()
                .filter(s -> Boolean.TRUE.equals(s.getCompletada())
                          && s.getPorcentajeAciertos() != null)
                .sorted(Comparator.comparing(SesionJuego::getInicio))
                .collect(Collectors.toList());

        // CA-03: no hay suficiente historial todavía
        if (sesiones.size() < SESIONES_MINIMAS) {
            log.debug("IA: perfil={} juego={} → solo {} sesiones, se necesitan {}.",
                    perfilId, juegoId, sesiones.size(), SESIONES_MINIMAS);
            return null;
        }

        // 2. Tomar solo la ventana de las últimas N sesiones
        if (sesiones.size() > SESIONES_VENTANA) {
            sesiones = sesiones.subList(sesiones.size() - SESIONES_VENTANA, sesiones.size());
        }

        // 3. Calcular tendencia mediante diferencias pairwise de porcentajeAciertos
        List<BigDecimal> scores = sesiones.stream()
                .map(SesionJuego::getPorcentajeAciertos)
                .collect(Collectors.toList());

        int mejoras = 0, regresiones = 0, empates = 0;
        for (int i = 1; i < scores.size(); i++) {
            int cmp = scores.get(i).compareTo(scores.get(i - 1));
            if (cmp > 0) mejoras++;
            else if (cmp < 0) regresiones++;
            else empates++;
        }

        int totalDiferencias = scores.size() - 1;
        double confMejora    = (double) mejoras    / totalDiferencias;
        double confRegresion = (double) regresiones / totalDiferencias;

        // 4. Determinar tendencia y confianza
        String tendencia;
        double confianza;
        if (mejoras > regresiones && confMejora >= CONFIANZA_MINIMA) {
            tendencia  = "MEJORA";
            confianza  = confMejora;
        } else if (regresiones > mejoras && confRegresion >= CONFIANZA_MINIMA) {
            tendencia  = "REGRESION";
            confianza  = confRegresion;
        } else {
            tendencia  = "ESTANCAMIENTO";
            confianza  = (totalDiferencias > 0)
                    ? (double) empates / totalDiferencias
                    : 1.0;
        }

        // 5. Nivel actual (de la sesión más reciente de la ventana)
        SesionJuego ultimaSesion = sesiones.get(sesiones.size() - 1);
        NivelDificultad nivelActual = ultimaSesion.getNivel();

        // 6. Obtener todos los niveles del juego ordenados por ID (FACIL→EXPERTO)
        List<NivelDificultad> niveles = nivelDificultadRepository
                .findByJuegoId(juegoId)
                .stream()
                .sorted(Comparator.comparing(NivelDificultad::getId))
                .collect(Collectors.toList());

        int posActual = nivelesIndexOf(niveles, nivelActual.getId());

        // 7. CA-01: aplicar regla de recomendación
        NivelDificultad nivelRecomendado = nivelActual;
        String motivo;

        if ("MEJORA".equals(tendencia) && posActual < niveles.size() - 1) {
            nivelRecomendado = niveles.get(posActual + 1);
            motivo = String.format(
                "Mejora consistente: %d de %d sesiones mejoraron (confianza %.0f%%). "
                + "Nivel ascendido de %s a %s.",
                mejoras, totalDiferencias, confianza * 100,
                nivelActual.getNivel(), nivelRecomendado.getNivel());

        } else if ("MEJORA".equals(tendencia)) {
            // Ya está en el nivel máximo
            motivo = String.format(
                "Mejora consistente (confianza %.0f%%) pero ya se encuentra en el nivel máximo %s.",
                confianza * 100, nivelActual.getNivel());

        } else if ("REGRESION".equals(tendencia) && posActual > 0) {
            nivelRecomendado = niveles.get(posActual - 1);
            motivo = String.format(
                "Regresión detectada: %d de %d sesiones empeoraron (confianza %.0f%%). "
                + "Nivel reducido de %s a %s.",
                regresiones, totalDiferencias, confianza * 100,
                nivelActual.getNivel(), nivelRecomendado.getNivel());

        } else if ("REGRESION".equals(tendencia)) {
            // CA-01: mínimo nivel 1, no bajar más
            motivo = String.format(
                "Regresión detectada (confianza %.0f%%) pero ya se encuentra en el nivel mínimo %s. "
                + "Se mantiene el nivel.",
                confianza * 100, nivelActual.getNivel());

        } else {
            motivo = String.format(
                "Desempeño estable en las últimas %d sesiones. Se mantiene el nivel %s.",
                sesiones.size(), nivelActual.getNivel());
        }

        // 8. CA-05: traza de sesiones que originaron esta recomendación
        String sesionesJson = sesiones.stream()
                .map(s -> s.getId().toString())
                .collect(Collectors.joining(",", "[", "]"));

        // 9. Persistir (CA-02)
        IaRecomendacion rec = IaRecomendacion.builder()
                .perfil(ultimaSesion.getPerfil())
                .juego(ultimaSesion.getJuego())
                .nivelRecomendado(nivelRecomendado)
                .nivelAnterior(nivelActual)
                .tendencia(tendencia)
                .confianza(BigDecimal.valueOf(confianza).setScale(4, RoundingMode.HALF_UP))
                .motivo(motivo)
                .fechaRecomendacion(LocalDateTime.now())
                .sesionesOrigen(sesionesJson)
                .build();

        IaRecomendacion saved = recomendacionRepository.save(rec);
        log.info("IA: perfil={} juego={} → {} (confianza={:.0%}) nivel {}.",
                perfilId, juegoId, tendencia, confianza, nivelRecomendado.getNivel());
        return saved;
    }

    /**
     * CA-04: Solo ADMINISTRADOR puede sobrescribir la recomendación.
     * El controlador se encarga de verificar el rol.
     */
    @Transactional
    public IaRecomendacion sobrescribirNivel(Integer recomendacionId, Integer nuevoNivelId) {
        IaRecomendacion rec = recomendacionRepository.findById(recomendacionId)
                .orElseThrow(() -> new RuntimeException("Recomendación no encontrada: " + recomendacionId));
        NivelDificultad nuevo = nivelDificultadRepository.findById(nuevoNivelId)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado: " + nuevoNivelId));

        rec.setNivelAnterior(rec.getNivelRecomendado());
        rec.setNivelRecomendado(nuevo);
        rec.setTendencia("SOBRESCRITO");
        rec.setMotivo("Sobrescrito manualmente por administrador. Nuevo nivel: " + nuevo.getNivel());
        rec.setFechaRecomendacion(LocalDateTime.now());
        return recomendacionRepository.save(rec);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Helper
    // ═══════════════════════════════════════════════════════════════════════

    private int nivelesIndexOf(List<NivelDificultad> niveles, Integer nivelId) {
        for (int i = 0; i < niveles.size(); i++) {
            if (niveles.get(i).getId().equals(nivelId)) return i;
        }
        return 0; // fallback al primer nivel
    }
}
