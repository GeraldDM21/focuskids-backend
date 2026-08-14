package cr.cenfotec.focuskids_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cr.cenfotec.focuskids_backend.dto.juego.FinalizarSesionRequest;
import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SesionService {

    private final SesionJuegoRepository sesionJuegoRepository;
    private final SessionClickEventRepository clickEventRepository;
    private final MetricaRepository metricaRepository;
    private final PerfilNinoRepository perfilNinoRepository;
    private final JuegoRepository juegoRepository;
    private final NivelDificultadRepository nivelDificultadRepository;
    private final NivelAsignadoRepository nivelAsignadoRepository;
    private final IaEvaluacionService iaEvaluacionService;
    private final NotificacionService notificacionService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public SesionJuego iniciarSesion(Integer perfilId, Integer juegoId, Integer nivelId) {
        PerfilNino perfil = perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilId));
        Juego juego = juegoRepository.findById(juegoId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado: " + juegoId));

        // El niño solo puede jugar el nivel que el docente/padre le fijó para este
        // juego: si existe un NivelAsignado, se ignora el nivelId que mande el
        // cliente y se fuerza el nivel bloqueado, sin importar qué haya elegido
        // (o intentado forzar) desde la UI.
        Integer nivelIdEfectivo = nivelAsignadoRepository.findByPerfilIdAndJuegoId(perfilId, juegoId)
                .flatMap(bloqueo -> nivelDificultadRepository.findByJuegoIdAndNivel(juegoId, bloqueo.getNivel()))
                .map(NivelDificultad::getId)
                .orElse(nivelId);

        NivelDificultad nivel = nivelDificultadRepository.findById(nivelIdEfectivo)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado: " + nivelIdEfectivo));

        SesionJuego sesion = SesionJuego.builder()
                .perfil(perfil)
                .juego(juego)
                .nivel(nivel)
                .inicio(LocalDateTime.now())
                .completada(false)
                .build();

        return sesionJuegoRepository.save(sesion);
    }

    @Transactional
    public SesionJuego finalizarSesion(Integer sesionId, FinalizarSesionRequest req) {
        SesionJuego sesion = sesionJuegoRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + sesionId));

        LocalDateTime fin = LocalDateTime.now();
        sesion.setFin(fin);
        sesion.setCompletada(true);

        // ── CA-01: métricas básicas ───────────────────────────────────────
        sesion.setPuntaje(req.getPuntaje());
        sesion.setTotalIntentos(req.getTotalIntentos());
        sesion.setTotalAciertos(req.getTotalAciertos());
        sesion.setPorcentajeAciertos(req.getPorcentajeAciertos());
        sesion.setTiempoRespuestaPromedioMs(req.getTiempoRespuestaPromedioMs());
        sesion.setRachaMaxAciertos(req.getRachaMaxAciertos());

        // ── CA-01: duración calculada desde inicio ────────────────────────
        if (sesion.getInicio() != null) {
            long segundos = ChronoUnit.SECONDS.between(sesion.getInicio(), fin);
            sesion.setDuracionSesionSegundos((int) segundos);
        }

        // ── CA-02: versión de configuración ──────────────────────────────
        sesion.setConfigVersion(req.getConfigVersion());

        // ── CA-05: concentración baja ─────────────────────────────────────
        sesion.setSesionConcentracionBaja(
                req.getSesionConcentracionBaja() != null && req.getSesionConcentracionBaja()
        );

        // ── CA-09: fallos por zona (JSON guardado como texto) ─────────────
        sesion.setIntentosFallidosPorZona(req.getIntentosFallidosPorZona());

        // ── Motor de IA / CA-01: la sesión es "válida" para análisis si se
        // completó y tiene métricas de aciertos utilizables ────────────────
        sesion.setSesionValida(calcularSesionValida(sesion));

        SesionJuego guardada = sesionJuegoRepository.save(sesion);

        // ── Notificar al docente cuando el alumno termina una sesión ──────
        Docente docente = guardada.getPerfil().getDocente();
        if (docente != null && docente.getUsuario() != null) {
            notificacionService.crear(
                docente.getUsuario().getId(),
                "SESION_COMPLETADA",
                guardada.getPerfil().getNombre() + " completó una sesión de "
                    + guardada.getJuego().getNombre() + ". Podés revisar y ajustar su nivel de dificultad."
            );
        }

        // ── Métrica agregada para reportes (docente/padre) ────────────────
        // Algunos juegos (Cascada Numérica, Lab Ciencias, Laberinto Cognitivo,
        // Maratón Mental) ya generan su propia Métrica más rica desde su
        // servicio dedicado, usando el mismo patrón de "upsert" (buscar por
        // sesionId antes de crear). Este bloque genérico es la base para
        // TODOS los juegos — se ejecuta primero o después según el orden de
        // llamadas del frontend, y en ambos casos el resultado final es
        // correcto: si ya existe una Métrica para esta sesión, se actualiza
        // en vez de duplicarla.
        Metrica metrica = metricaRepository.findBySesionId(sesionId)
                .orElseGet(() -> Metrica.builder().sesion(guardada).build());
        metrica.setTiempoReaccionProm(req.getTiempoRespuestaPromedioMs());
        metrica.setPrecisionPct(req.getPorcentajeAciertos());
        metrica.setErrores(
                req.getTotalIntentos() != null && req.getTotalAciertos() != null
                        ? Math.max(0, req.getTotalIntentos() - req.getTotalAciertos())
                        : null
        );
        metrica.setZonaFallo(determinarZonaFallo(req.getIntentosFallidosPorZona()));
        metricaRepository.save(metrica);

        // ── CA-03/CA-04: dispara el análisis de tendencia en segundo plano.
        // No bloquea esta respuesta; cualquier error queda sólo en logs.
        iaEvaluacionService.evaluarAsync(
                guardada.getPerfil().getId(),
                guardada.getJuego().getId(),
                guardada.getNivel().getId()
        );

        return guardada;
    }

    /**
     * Criterio de validez de una sesión para el Motor de IA: debe estar
     * completada y traer métricas reales de aciertos (evita dividir/analizar
     * sobre sesiones abandonadas o sin intentos registrados).
     */
    private boolean calcularSesionValida(SesionJuego sesion) {
        return Boolean.TRUE.equals(sesion.getCompletada())
                && sesion.getTotalIntentos() != null && sesion.getTotalIntentos() > 0
                && sesion.getPorcentajeAciertos() != null;
    }

    @Transactional
    public SessionClickEvent registrarEvento(Integer sesionId, SessionClickEvent evento) {
        SesionJuego sesion = sesionJuegoRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + sesionId));
        evento.setSesion(sesion);
        return clickEventRepository.save(evento);
    }

    public List<SesionJuego> obtenerPorPerfil(Integer perfilId) {
        return sesionJuegoRepository.findByPerfilIdOrderByInicioDesc(perfilId);
    }

    public List<SessionClickEvent> obtenerEventos(Integer sesionId) {
        return clickEventRepository.findBySesionId(sesionId);
    }

    /**
     * CA-09: a partir del JSON de fallos por cuadrante que manda el frontend
     * (ej. {"superiorIzquierdo":2,"superiorDerecho":0,...}), determina cuál
     * cuadrante concentró más fallos. Devuelve null si no hay fallos o el
     * JSON no se pudo leer.
     */
    private String determinarZonaFallo(String intentosFallidosPorZonaJson) {
        if (intentosFallidosPorZonaJson == null || intentosFallidosPorZonaJson.isBlank()) {
            return null;
        }
        try {
            JsonNode nodo = mapper.readTree(intentosFallidosPorZonaJson);
            String zonaMax = null;
            int maxValor = 0;
            Iterator<Map.Entry<String, JsonNode>> campos = nodo.fields();
            while (campos.hasNext()) {
                Map.Entry<String, JsonNode> campo = campos.next();
                int valor = campo.getValue().asInt(0);
                if (valor > maxValor) {
                    maxValor = valor;
                    zonaMax = campo.getKey();
                }
            }
            return zonaMax;
        } catch (Exception e) {
            log.warn("No se pudo leer intentosFallidosPorZona: {}", e.getMessage());
            return null;
        }
    }
}
