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

        NivelDificultad nivelInicial = obtenerNivel(juego.getId(), NIVEL_MINIMO);

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
                .nivelInicial(NIVEL_MINIMO)
                .tamanoMapa(TAMANO_BASE + NIVEL_MINIMO)
                .obstaculosDinamicos(NIVEL_MINIMO >= NIVEL_OBSTACULOS_DESDE)
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
                .nivel(limitarNivel(request.getNivel()))
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

        if (Boolean.TRUE.equals(sesion.getCompletada())) {
            throw new IllegalStateException("La sesión ya fue finalizada");
        }

        double eficiencia = request.getPasosUsadosTotal() == 0
                ? 0
                : Math.min(100.0, (request.getPasosOptimosTotal() * 100.0) / request.getPasosUsadosTotal());

        int puntaje = calcularPuntaje(request, eficiencia);

        NivelDificultad nivelFinal = obtenerNivel(
                sesion.getJuego().getId(),
                limitarNivel(request.getNivelMaximoAlcanzado())
        );

        sesion.setFin(LocalDateTime.now());
        sesion.setPuntaje(puntaje);
        sesion.setCompletada(true);
        sesion.setNivel(nivelFinal);

        sesionJuegoRepository.save(sesion);

        double tiempoPromedioPorPaso = request.getPasosUsadosTotal() == 0
                ? 0
                : (double) request.getTiempoResolucionMsTotal() / request.getPasosUsadosTotal();

        Metrica metrica = Metrica.builder()
                .sesion(sesion)
                .tiempoReaccionProm(decimal(tiempoPromedioPorPaso))
                .precisionPct(decimal(eficiencia))
                .errores(request.getCallejonesSinSalidaVisitadosTotal())
                .zonaFallo(determinarZonaFallo(request))
                .build();

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
                .nivelMaximoAlcanzado(limitarNivel(request.getNivelMaximoAlcanzado()))
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

    private int calcularPuntaje(FinalizarLaberintoRequest request, double eficiencia) {
        int puntaje = (int) Math.round(eficiencia * 10)
                + request.getRondasCompletadas() * 30
                + limitarNivel(request.getNivelMaximoAlcanzado()) * 40
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

    private BigDecimal decimal(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    private double redondear(double valor) {
        return decimal(valor).doubleValue();
    }
}
