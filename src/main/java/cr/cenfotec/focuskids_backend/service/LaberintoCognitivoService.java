package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.juego.*;
import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// RF-29: Laberinto Cognitivo — planificación y función ejecutiva.
// Una "sesión" cubre varias rondas (laberintos) consecutivas jugadas de una sentada;
// el nivel/tamaño del laberinto sube o baja de una ronda a la siguiente según la
// eficiencia con la que el niño resolvió la ronda anterior (calculado en el frontend,
// igual que Cascada Numérica calcula su propio ajuste en vivo). El backend registra
// cada paso y, al finalizar toda la sesión, guarda el resumen para el motor de reportes.
@Service
@RequiredArgsConstructor
public class LaberintoCognitivoService {

    private static final String NOMBRE_JUEGO = "Laberinto Cognitivo";

    private static final int NIVEL_MINIMO = 1;
    private static final int NIVEL_MAXIMO = 5;
    private static final int TAMANO_BASE = 4; // tamano = TAMANO_BASE + nivel -> nivel1=5x5 (CA-02)
    private static final int NIVEL_OBSTACULOS_DESDE = 3; // CA-04
    private static final int TIEMPO_DESPLIEGUE_MS = 3000; // CA-01
    private static final double UMBRAL_EFICIENCIA_SUBIDA = 1.30; // CA-06

    private static final Set<String> DIRECCIONES_VALIDAS =
            Set.of("ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA");

    private final SesionJuegoRepository sesionJuegoRepository;
    private final JuegoRepository juegoRepository;
    private final NivelDificultadRepository nivelDificultadRepository;
    private final PerfilNinoRepository perfilNinoRepository;
    private final LaberintoPasoEventoRepository pasoRepository;
    private final MetricaRepository metricaRepository;
    private final NivelAsignadoRepository nivelAsignadoRepository;

    @Transactional
    public IniciarLaberintoResponse iniciarSesion(IniciarLaberintoRequest request) {
        PerfilNino perfil = perfilNinoRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el perfil infantil"));

        if (Boolean.FALSE.equals(perfil.getActivo())) {
            throw new IllegalStateException("El perfil infantil se encuentra inactivo");
        }

        Juego juego = juegoRepository.findByNombreIgnoreCase(NOMBRE_JUEGO)
                .orElseThrow(() -> new IllegalStateException("Laberinto Cognitivo no está registrado"));

        if (Boolean.FALSE.equals(juego.getActivo())) {
            throw new IllegalStateException("Laberinto Cognitivo se encuentra desactivado");
        }

        // El niño solo puede jugar el nivel que el docente/padre le fijó: si hay
        // un NivelAsignado, la sesión arranca directo en ese nivel en vez de
        // NIVEL_MINIMO, y registrarPaso/finalizarSesion lo mantienen fijo ahí
        // durante toda la sesión (ver limitarNivelSesion).
        Integer nivelBloqueado = obtenerNivelBloqueado(perfil.getId(), juego.getId());
        int nivelInicioSesion = nivelBloqueado != null ? nivelBloqueado : NIVEL_MINIMO;

        NivelDificultad nivelInicial = obtenerNivel(juego.getId(), nivelInicioSesion);

        SesionJuego sesion = SesionJuego.builder()
                .perfil(perfil)
                .juego(juego)
                .nivel(nivelInicial)
                .inicio(LocalDateTime.now())
                .fin(null)
                .puntaje(0)
                .completada(false)
                .build();

        SesionJuego guardada = sesionJuegoRepository.save(sesion);

        return IniciarLaberintoResponse.builder()
                .sesionId(guardada.getId())
                .perfilId(perfil.getId())
                .juegoId(juego.getId())
                .nivelId(nivelInicial.getId())
                .nivelInicial(nivelInicioSesion)
                .tamanoMapa(TAMANO_BASE + nivelInicioSesion)
                .obstaculosDinamicos(nivelInicioSesion >= NIVEL_OBSTACULOS_DESDE)
                .tiempoDespliegueMs(TIEMPO_DESPLIEGUE_MS)
                .build();
    }

    @Transactional
    public RegistrarPasoResponse registrarPaso(Integer sesionId, RegistrarPasoRequest request) {
        SesionJuego sesion = obtenerSesion(sesionId);
        validarSesionDisponible(sesion);
        validarPaso(request);

        long cantidadActual = pasoRepository.countBySesionId(sesionId);

        if (!request.getNumeroPaso().equals((int) cantidadActual + 1)) {
            throw new IllegalArgumentException("El número de paso no corresponde con la secuencia esperada");
        }

        LaberintoPasoEvento evento = LaberintoPasoEvento.builder()
                .sesion(sesion)
                .numeroPaso(request.getNumeroPaso())
                .direccion(request.getDireccion().toUpperCase())
                .posicionX(request.getPosicionX())
                .posicionY(request.getPosicionY())
                .esCallejonSinSalida(request.getEsCallejonSinSalida())
                .tiempoDesdeInicioMs(request.getTiempoDesdeInicioMs())
                .nivel(limitarNivelSesion(sesion, request.getNivel()))
                .build();

        LaberintoPasoEvento guardado = pasoRepository.save(evento);

        long callejones = pasoRepository.countBySesionIdAndEsCallejonSinSalidaTrue(sesionId);

        return RegistrarPasoResponse.builder()
                .pasoId(guardado.getId())
                .pasosRegistrados(Math.toIntExact(cantidadActual + 1))
                .callejonesSinSalidaHastaAhora(Math.toIntExact(callejones))
                .build();
    }

    @Transactional
    public LaberintoResultadoResponse finalizarSesion(Integer sesionId, FinalizarLaberintoRequest request) {
        SesionJuego sesion = obtenerSesion(sesionId);

        // Nota: el frontend llama primero a SesionJuegoService.finalizarSesion()
        // (endpoint genérico, que ya marca completada=true) y justo después a
        // este endpoint dedicado con el detalle de Laberinto. Antes esto lanzaba
        // una excepción si la llamada genérica llegaba primero (carrera entre
        // dos peticiones HTTP separadas), lo que podía perder silenciosamente
        // la Métrica detallada de Laberinto. Ahora se sigue procesando de forma
        // idempotente: los campos de sesión se recalculan igual y la Métrica se
        // actualiza (upsert) en vez de duplicarse.
        double eficiencia = request.getPasosUsadosTotal() == 0
                ? 0
                : Math.min(100.0, (request.getPasosOptimosTotal() * 100.0) / request.getPasosUsadosTotal());

        // Si hay un nivel bloqueado para este perfil+juego, el nivel máximo
        // alcanzado que reporte el cliente se ignora — la sesión completa se
        // jugó (y se puntúa) en el nivel fijado por el docente/padre.
        int nivelMaximoEfectivo = limitarNivelSesion(sesion, request.getNivelMaximoAlcanzado());

        int puntaje = calcularPuntaje(request, eficiencia, nivelMaximoEfectivo);

        NivelDificultad nivelFinal = obtenerNivel(
                sesion.getJuego().getId(),
                nivelMaximoEfectivo
        );

        sesion.setFin(LocalDateTime.now());
        sesion.setPuntaje(puntaje);
        sesion.setCompletada(true);
        sesion.setNivel(nivelFinal);

        sesionJuegoRepository.save(sesion);

        double tiempoPromedioPorPaso = request.getPasosUsadosTotal() == 0
                ? 0
                : (double) request.getTiempoResolucionMsTotal() / request.getPasosUsadosTotal();

        Metrica metrica = metricaRepository.findBySesionId(sesion.getId())
                .orElseGet(() -> Metrica.builder().sesion(sesion).build());
        metrica.setTiempoReaccionProm(decimal(tiempoPromedioPorPaso));
        metrica.setPrecisionPct(decimal(eficiencia));
        metrica.setErrores(request.getCallejonesSinSalidaVisitadosTotal());
        metrica.setZonaFallo(determinarZonaFallo(request));

        metricaRepository.save(metrica);

        return LaberintoResultadoResponse.builder()
                .sesionId(sesion.getId())
                .rondasCompletadas(request.getRondasCompletadas())
                .pasosUsadosTotal(request.getPasosUsadosTotal())
                .pasosOptimosTotal(request.getPasosOptimosTotal())
                .porcentajeEficiencia(redondear(eficiencia))
                .tiempoResolucionMsTotal(request.getTiempoResolucionMsTotal())
                .callejonesSinSalidaVisitadosTotal(request.getCallejonesSinSalidaVisitadosTotal())
                .planificoEnPrimerMovimiento(request.getPlanificoEnPrimerMovimiento())
                .nivelMaximoAlcanzado(nivelMaximoEfectivo)
                .puntaje(puntaje)
                .completada(true)
                .build();
    }

    @Transactional(readOnly = true)
    public List<LaberintoPasoEvento> obtenerPasos(Integer sesionId) {
        obtenerSesion(sesionId);
        return pasoRepository.findBySesionIdOrderByNumeroPasoAsc(sesionId);
    }

    private SesionJuego obtenerSesion(Integer sesionId) {
        SesionJuego sesion = sesionJuegoRepository.findById(sesionId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la sesión"));

        if (sesion.getJuego() == null || !NOMBRE_JUEGO.equalsIgnoreCase(sesion.getJuego().getNombre())) {
            throw new IllegalArgumentException("La sesión no pertenece a Laberinto Cognitivo");
        }

        return sesion;
    }

    private void validarSesionDisponible(SesionJuego sesion) {
        if (Boolean.TRUE.equals(sesion.getCompletada())) {
            throw new IllegalStateException("La sesión ya fue finalizada");
        }
    }

    private void validarPaso(RegistrarPasoRequest request) {
        String direccion = request.getDireccion().toUpperCase();
        if (!DIRECCIONES_VALIDAS.contains(direccion)) {
            throw new IllegalArgumentException("direccion debe ser ARRIBA, ABAJO, IZQUIERDA o DERECHA");
        }
    }

    private int calcularPuntaje(FinalizarLaberintoRequest request, double eficiencia, int nivelMaximoAlcanzado) {
        int puntaje = (int) Math.round(eficiencia * 10)
                + request.getRondasCompletadas() * 30
                + nivelMaximoAlcanzado * 40
                - request.getCallejonesSinSalidaVisitadosTotal() * 15;

        return Math.max(0, puntaje);
    }

    private String determinarZonaFallo(FinalizarLaberintoRequest request) {
        if (Boolean.FALSE.equals(request.getPlanificoEnPrimerMovimiento())) {
            return "PLANIFICACION_PREVIA";
        }

        if (request.getCallejonesSinSalidaVisitadosTotal() > request.getRondasCompletadas() * 2) {
            return "EXPLORACION_INEFICIENTE";
        }

        if (request.getPasosUsadosTotal() > request.getPasosOptimosTotal() * UMBRAL_EFICIENCIA_SUBIDA) {
            return "EFICIENCIA_DE_RUTA";
        }

        return "SIN_FALLOS_RELEVANTES";
    }

    private NivelDificultad obtenerNivel(Integer juegoId, int nivel) {
        int nivelLimitado = limitarNivel(nivel);

        String nombreNivel = switch (nivelLimitado) {
            case 1, 2 -> "FACIL";
            case 3 -> "MEDIO";
            case 4, 5 -> "DIFICIL";
            default -> "FACIL";
        };

        return nivelDificultadRepository.findByJuegoIdAndNivel(juegoId, nombreNivel)
                .orElseThrow(() -> new IllegalStateException(
                        "No se encontró el nivel " + nombreNivel + " de Laberinto Cognitivo"
                ));
    }

    private int limitarNivel(int nivel) {
        return Math.max(NIVEL_MINIMO, Math.min(NIVEL_MAXIMO, nivel));
    }

    /**
     * Nivel numérico (1-5) fijado por docente/padre para este perfil+juego,
     * o null si no hay bloqueo. FACIL/MEDIO/DIFICIL de NivelAsignado se
     * mapean al valor representativo de cada franja (igual agrupación que
     * obtenerNivel: 1-2 FACIL, 3 MEDIO, 4-5 DIFICIL).
     */
    private Integer obtenerNivelBloqueado(Integer perfilId, Integer juegoId) {
        Optional<NivelAsignado> bloqueo = nivelAsignadoRepository.findByPerfilIdAndJuegoId(perfilId, juegoId);
        if (bloqueo.isEmpty()) {
            return null;
        }
        return switch (bloqueo.get().getNivel()) {
            case "FACIL" -> Integer.valueOf(NIVEL_MINIMO);
            case "MEDIO" -> Integer.valueOf(3);
            case "DIFICIL" -> Integer.valueOf(NIVEL_MAXIMO);
            default -> null;
        };
    }

    /**
     * Si el perfil tiene un nivel bloqueado para este juego, el nivel se fuerza
     * a ese valor sin importar lo que reporte el cliente; si no, se aplica el
     * límite normal [NIVEL_MINIMO, NIVEL_MAXIMO].
     */
    private int limitarNivelSesion(SesionJuego sesion, int nivel) {
        Integer bloqueado = obtenerNivelBloqueado(sesion.getPerfil().getId(), sesion.getJuego().getId());
        return bloqueado != null ? bloqueado : limitarNivel(nivel);
    }

    private BigDecimal decimal(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    private double redondear(double valor) {
        return decimal(valor).doubleValue();
    }
}
