package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.juego.FinalizarMaratonRequest;
import cr.cenfotec.focuskids_backend.dto.juego.IniciarMaratonRequest;
import cr.cenfotec.focuskids_backend.dto.juego.IniciarMaratonResponse;
import cr.cenfotec.focuskids_backend.dto.juego.MaratonResultadoResponse;
import cr.cenfotec.focuskids_backend.dto.juego.RegistrarRondaMaratonRequest;
import cr.cenfotec.focuskids_backend.dto.juego.RegistrarRondaMaratonResponse;
import cr.cenfotec.focuskids_backend.model.Juego;
import cr.cenfotec.focuskids_backend.model.MaratonMentalRondaEvento;
import cr.cenfotec.focuskids_backend.model.Metrica;
import cr.cenfotec.focuskids_backend.model.NivelDificultad;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.SesionJuego;
import cr.cenfotec.focuskids_backend.repository.JuegoRepository;
import cr.cenfotec.focuskids_backend.repository.MaratonMentalRondaEventoRepository;
import cr.cenfotec.focuskids_backend.repository.MetricaRepository;
import cr.cenfotec.focuskids_backend.repository.NivelDificultadRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import cr.cenfotec.focuskids_backend.repository.SesionJuegoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * RF-36: Maratón Mental — Juego Cognitivo Adaptativo.
 *
 * Dos tareas simultáneas ("Contar Objetos" = Tarea A, "Identificar Color" = Tarea B).
 * Cada sesión pasa por 3 fases: calibración individual de A, calibración individual
 * de B, y luego rondas duales (pantalla dividida, ambas tareas a la vez).
 *
 * El "motor IA" de este juego (CA-03/04/05/06) compara el rendimiento de cada tarea
 * en solitario (calibración) contra su rendimiento en modo dual, para saber cuánto
 * le cuesta al niño repartir su atención. Esa diferencia es el "índice de costo dual".
 */
@Service
@RequiredArgsConstructor
public class MaratonMentalService {

    private static final String NOMBRE_JUEGO = "Maratón Mental";

    private static final List<String> NIVELES = List.of("FACIL", "MEDIO", "DIFICIL", "EXPERTO");

    public static final int CALIBRACION_RONDAS_POR_TAREA = 3;

    // Umbral CA-05: si el costo dual de una tarea supera este %, se reduce su dificultad.
    private static final double UMBRAL_COSTO_DUAL = 40.0;

    // Muestra mínima de rondas duales antes de evaluar el costo (evita decisiones con 1 dato).
    private static final int MINIMO_RONDAS_DUALES_PARA_EVALUAR = 3;

    private record ConfigNivel(
            int rondasDuales, int tiempoRondaMs,
            int objetosMin, int objetosMax, int opcionesConteo, int opcionesColor
    ) {}

    private static final Map<String, ConfigNivel> CONFIG = Map.of(
            "FACIL",   new ConfigNivel(8, 7000, 2, 5, 3, 3),
            "MEDIO",   new ConfigNivel(10, 6000, 3, 7, 4, 4),
            "DIFICIL", new ConfigNivel(12, 5000, 4, 9, 4, 5),
            "EXPERTO", new ConfigNivel(14, 4500, 5, 12, 5, 6)
    );

    private final SesionJuegoRepository sesionRepository;
    private final JuegoRepository juegoRepository;
    private final NivelDificultadRepository nivelRepository;
    private final PerfilNinoRepository perfilRepository;
    private final MaratonMentalRondaEventoRepository rondaRepository;
    private final MetricaRepository metricaRepository;

    @Transactional
    public IniciarMaratonResponse iniciarSesion(IniciarMaratonRequest request) {
        String nivel = normalizarNivel(request.getNivel());

        PerfilNino perfil = perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el perfil infantil"));

        if (Boolean.FALSE.equals(perfil.getActivo())) {
            throw new IllegalStateException("El perfil infantil se encuentra inactivo");
        }

        Juego juego = juegoRepository.findByNombreIgnoreCase(NOMBRE_JUEGO)
                .orElseThrow(() -> new IllegalStateException("Maratón Mental no está registrado"));

        if (Boolean.FALSE.equals(juego.getActivo())) {
            throw new IllegalStateException("Maratón Mental está desactivado");
        }

        NivelDificultad nivelBd = obtenerNivelBd(juego.getId(), nivel);
        ConfigNivel cfg = CONFIG.get(nivel);

        SesionJuego sesion = SesionJuego.builder()
                .perfil(perfil)
                .juego(juego)
                .nivel(nivelBd)
                .inicio(LocalDateTime.now())
                .puntaje(0)
                .completada(false)
                .build();

        sesion = sesionRepository.save(sesion);

        return IniciarMaratonResponse.builder()
                .sesionId(sesion.getId())
                .perfilId(perfil.getId())
                .juegoId(juego.getId())
                .nivelId(nivelBd.getId())
                .nivelSeleccionado(nivel)
                .rondasCalibracionPorTarea(CALIBRACION_RONDAS_POR_TAREA)
                .rondasDuales(cfg.rondasDuales())
                .tiempoRondaMs(cfg.tiempoRondaMs())
                .objetosMin(cfg.objetosMin())
                .objetosMax(cfg.objetosMax())
                .opcionesConteo(cfg.opcionesConteo())
                .opcionesColor(cfg.opcionesColor())
                .build();
    }

    @Transactional
    public RegistrarRondaMaratonResponse registrarRonda(Integer sesionId, RegistrarRondaMaratonRequest request) {
        SesionJuego sesion = obtenerSesion(sesionId);

        if (Boolean.TRUE.equals(sesion.getCompletada())) {
            throw new IllegalStateException("La sesión ya fue finalizada");
        }

        String fase = normalizarFase(request.getFase());
        String nivel = normalizarNivel(request.getNivel());

        MaratonMentalRondaEvento ronda = MaratonMentalRondaEvento.builder()
                .sesion(sesion)
                .numeroRonda(request.getNumeroRonda())
                .fase(fase)
                .tareaARespondida(Boolean.TRUE.equals(request.getTareaARespondida()))
                .tareaACorrecta(request.getTareaARespondida() != null && request.getTareaARespondida()
                        ? Boolean.TRUE.equals(request.getTareaACorrecta()) : false)
                .tareaATiempoRespuestaMs(request.getTareaATiempoRespuestaMs())
                .tareaBRespondida(Boolean.TRUE.equals(request.getTareaBRespondida()))
                .tareaBCorrecta(request.getTareaBRespondida() != null && request.getTareaBRespondida()
                        ? Boolean.TRUE.equals(request.getTareaBCorrecta()) : false)
                .tareaBTiempoRespuestaMs(request.getTareaBTiempoRespuestaMs())
                .nivel(nivel)
                .build();

        ronda = rondaRepository.save(ronda);

        List<MaratonMentalRondaEvento> eventos = rondaRepository.findBySesionIdOrderByIdAsc(sesionId);

        TaskStats statsA = calcularStats(eventos, true);
        TaskStats statsB = calcularStats(eventos, false);

        boolean suficienteMuestraA = statsA.totalIndividual() >= CALIBRACION_RONDAS_POR_TAREA
                && statsA.totalDual() >= MINIMO_RONDAS_DUALES_PARA_EVALUAR;
        boolean suficienteMuestraB = statsB.totalIndividual() >= CALIBRACION_RONDAS_POR_TAREA
                && statsB.totalDual() >= MINIMO_RONDAS_DUALES_PARA_EVALUAR;

        return RegistrarRondaMaratonResponse.builder()
                .rondaId(ronda.getId())
                .rondasRegistradas(eventos.size())
                .reducirTareaA(suficienteMuestraA && statsA.costoDualPct() > UMBRAL_COSTO_DUAL)
                .reducirTareaB(suficienteMuestraB && statsB.costoDualPct() > UMBRAL_COSTO_DUAL)
                .costoDualAPorcentaje(suficienteMuestraA ? redondear(statsA.costoDualPct()) : null)
                .costoDualBPorcentaje(suficienteMuestraB ? redondear(statsB.costoDualPct()) : null)
                .build();
    }

    @Transactional
    public MaratonResultadoResponse finalizarSesion(Integer sesionId, FinalizarMaratonRequest request) {
        SesionJuego sesion = obtenerSesion(sesionId);
        String nivelFinal = normalizarNivel(request.getNivelFinal());

        List<MaratonMentalRondaEvento> eventos = rondaRepository.findBySesionIdOrderByIdAsc(sesionId);

        TaskStats statsA = calcularStats(eventos, true);
        TaskStats statsB = calcularStats(eventos, false);

        double indiceInterferencia = (statsA.costoDualPct() + statsB.costoDualPct()) / 2.0;

        // Incremento porcentual del tiempo de respuesta en dual vs individual (promedio de ambas tareas).
        double incrementoTiempoA = incrementoPorcentual(statsA.tiempoIndividualMs(), statsA.tiempoDualMs());
        double incrementoTiempoB = incrementoPorcentual(statsB.tiempoIndividualMs(), statsB.tiempoDualMs());
        double incrementoTiempoProm = (incrementoTiempoA + incrementoTiempoB) / 2.0;

        double cargaCognitiva = Math.max(0, Math.min(100,
                indiceInterferencia * 0.6 + incrementoTiempoProm * 0.4));

        String cargaCategoria = cargaCognitiva < 33 ? "BAJA" : cargaCognitiva < 66 ? "MEDIA" : "ALTA";

        // CA-06: la tarea con mayor costo dual es la más afectada por la atención dividida.
        String tareaMasDebil;
        double diferencia = statsA.costoDualPct() - statsB.costoDualPct();
        if (Math.abs(diferencia) < 5.0) {
            tareaMasDebil = "NINGUNA";
        } else {
            tareaMasDebil = diferencia > 0 ? "A" : "B";
        }

        int aciertosDualesTotales = statsA.aciertosDual() + statsB.aciertosDual();
        int erroresDualesTotales = (statsA.totalDual() - statsA.aciertosDual())
                + (statsB.totalDual() - statsB.aciertosDual());
        int rondasDualesCompletadas = Math.max(statsA.totalDual(), statsB.totalDual());

        ConfigNivel cfg = CONFIG.get(nivelFinal);
        boolean completoTodasLasRondas = cfg != null && rondasDualesCompletadas >= cfg.rondasDuales();

        int puntaje = Math.max(0, (int) Math.round(
                aciertosDualesTotales * 60.0
                        - erroresDualesTotales * 15.0
                        + (completoTodasLasRondas ? 150 : 0)
                        - indiceInterferencia
        ));

        String nivelSugerido = calcularNivelSugerido(nivelFinal, statsA, statsB, indiceInterferencia);

        if (!Boolean.TRUE.equals(sesion.getCompletada())) {
            sesion.setFin(LocalDateTime.now());
            sesion.setPuntaje(puntaje);
            sesion.setCompletada(true);
            sesionRepository.save(sesion);
        }

        Metrica metrica = metricaRepository.findBySesionId(sesionId)
                .orElseGet(() -> Metrica.builder().sesion(sesion).build());

        double tiempoReaccionCombinado = promedioSeguro(statsA.tiempoDualMs(), statsB.tiempoDualMs());
        double precisionCombinada = promedioSeguro(statsA.precisionDualPct(), statsB.precisionDualPct());

        metrica.setTiempoReaccionProm(BigDecimal.valueOf(tiempoReaccionCombinado).setScale(2, RoundingMode.HALF_UP));
        metrica.setPrecisionPct(BigDecimal.valueOf(precisionCombinada).setScale(2, RoundingMode.HALF_UP));
        metrica.setErrores(erroresDualesTotales);
        metrica.setZonaFallo("TAREA_" + tareaMasDebil + "_MAS_DEBIL");
        metricaRepository.save(metrica);

        return MaratonResultadoResponse.builder()
                .sesionId(sesionId)
                .precisionIndividualAPorcentaje(redondear(statsA.precisionIndividualPct()))
                .precisionIndividualBPorcentaje(redondear(statsB.precisionIndividualPct()))
                .precisionDualAPorcentaje(redondear(statsA.precisionDualPct()))
                .precisionDualBPorcentaje(redondear(statsB.precisionDualPct()))
                .tiempoRespuestaIndividualAMs(redondear(statsA.tiempoIndividualMs()))
                .tiempoRespuestaIndividualBMs(redondear(statsB.tiempoIndividualMs()))
                .tiempoRespuestaDualAMs(redondear(statsA.tiempoDualMs()))
                .tiempoRespuestaDualBMs(redondear(statsB.tiempoDualMs()))
                .costoDualAPorcentaje(redondear(statsA.costoDualPct()))
                .costoDualBPorcentaje(redondear(statsB.costoDualPct()))
                .indiceInterferenciaPorcentaje(redondear(indiceInterferencia))
                .cargaCognitivaEstimada(redondear(cargaCognitiva))
                .cargaCognitivaCategoria(cargaCategoria)
                .tareaMasDebil(tareaMasDebil)
                .rondasDualesCompletadas(rondasDualesCompletadas)
                .aciertosDualesTotales(aciertosDualesTotales)
                .erroresDualesTotales(erroresDualesTotales)
                .nivelFinal(nivelFinal)
                .nivelSugerido(nivelSugerido)
                .puntaje(puntaje)
                .completada(true)
                .build();
    }

    @Transactional(readOnly = true)
    public List<MaratonMentalRondaEvento> obtenerRondas(Integer sesionId) {
        obtenerSesion(sesionId);
        return rondaRepository.findBySesionIdOrderByIdAsc(sesionId);
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private record TaskStats(
            int totalIndividual, int aciertosIndividual, double precisionIndividualPct, double tiempoIndividualMs,
            int totalDual, int aciertosDual, double precisionDualPct, double tiempoDualMs,
            double costoDualPct
    ) {}

    /**
     * Calcula precisión y tiempo de respuesta de UNA tarea (A o B), tanto en su fase
     * de calibración individual como en la fase dual, y de ahí el índice de costo dual
     * (CA-04): cuánto cae el rendimiento al tener que atender la otra tarea a la vez.
     */
    private TaskStats calcularStats(List<MaratonMentalRondaEvento> eventos, boolean esTareaA) {
        String faseIndividual = esTareaA ? "CALIBRACION_A" : "CALIBRACION_B";

        List<MaratonMentalRondaEvento> individuales = eventos.stream()
                .filter(e -> faseIndividual.equals(e.getFase()))
                .toList();

        List<MaratonMentalRondaEvento> duales = eventos.stream()
                .filter(e -> "DUAL".equals(e.getFase()))
                .toList();

        int totalIndividual = individuales.size();
        long aciertosIndividual = individuales.stream()
                .filter(e -> Boolean.TRUE.equals(esTareaA ? e.getTareaACorrecta() : e.getTareaBCorrecta()))
                .count();
        double precisionIndividual = totalIndividual == 0 ? 0 : aciertosIndividual * 100.0 / totalIndividual;
        double tiempoIndividual = promedioTiempo(individuales, esTareaA);

        int totalDual = duales.size();
        long aciertosDual = duales.stream()
                .filter(e -> Boolean.TRUE.equals(esTareaA ? e.getTareaACorrecta() : e.getTareaBCorrecta()))
                .count();
        double precisionDual = totalDual == 0 ? 0 : aciertosDual * 100.0 / totalDual;
        double tiempoDual = promedioTiempo(duales, esTareaA);

        double costoDual = precisionIndividual <= 0
                ? 0
                : Math.max(0, (precisionIndividual - precisionDual) / precisionIndividual * 100.0);

        return new TaskStats(
                totalIndividual, (int) aciertosIndividual, precisionIndividual, tiempoIndividual,
                totalDual, (int) aciertosDual, precisionDual, tiempoDual,
                costoDual
        );
    }

    private double promedioTiempo(List<MaratonMentalRondaEvento> rondas, boolean esTareaA) {
        return rondas.stream()
                .filter(e -> Boolean.TRUE.equals(esTareaA ? e.getTareaACorrecta() : e.getTareaBCorrecta()))
                .map(e -> esTareaA ? e.getTareaATiempoRespuestaMs() : e.getTareaBTiempoRespuestaMs())
                .filter(t -> t != null)
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);
    }

    private double incrementoPorcentual(double individual, double dual) {
        if (individual <= 0) return 0;
        return Math.max(0, (dual - individual) / individual * 100.0);
    }

    private double promedioSeguro(double a, double b) {
        return (a + b) / 2.0;
    }

    private String calcularNivelSugerido(String nivelActual, TaskStats statsA, TaskStats statsB, double interferencia) {
        double precisionDualCombinada = (statsA.precisionDualPct() + statsB.precisionDualPct()) / 2.0;
        int indiceActual = NIVELES.indexOf(nivelActual);
        int nuevoIndice = indiceActual;

        if (precisionDualCombinada >= 70 && interferencia < UMBRAL_COSTO_DUAL && indiceActual < NIVELES.size() - 1) {
            nuevoIndice++;
        } else if ((precisionDualCombinada < 35 || interferencia >= UMBRAL_COSTO_DUAL) && indiceActual > 0) {
            nuevoIndice--;
        }

        return NIVELES.get(nuevoIndice);
    }

    private SesionJuego obtenerSesion(Integer id) {
        return sesionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la sesión"));
    }

    private String normalizarNivel(String nivel) {
        String nivelNormalizado = nivel == null ? "" : nivel.trim().toUpperCase();
        if (!NIVELES.contains(nivelNormalizado)) {
            throw new IllegalArgumentException("Nivel no válido");
        }
        return nivelNormalizado;
    }

    private String normalizarFase(String fase) {
        String faseNormalizada = fase == null ? "" : fase.trim().toUpperCase();
        if (!List.of("CALIBRACION_A", "CALIBRACION_B", "DUAL").contains(faseNormalizada)) {
            throw new IllegalArgumentException("Fase no válida: " + fase);
        }
        return faseNormalizada;
    }

    private NivelDificultad obtenerNivelBd(Integer juegoId, String nivel) {
        // La base de datos solo tiene FACIL/MEDIO/DIFICIL. EXPERTO reutiliza la fila
        // de DIFICIL para la sesión, pero el string "EXPERTO" se conserva en cada
        // ronda registrada (igual patrón que Lab de Ciencias).
        String nivelPersistido = nivel.equals("EXPERTO") ? "DIFICIL" : nivel;

        return nivelRepository.findByJuegoIdAndNivel(juegoId, nivelPersistido)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el nivel " + nivelPersistido + " para Maratón Mental"));
    }

    private Double redondear(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
