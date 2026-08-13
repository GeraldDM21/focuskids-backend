package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.juego.*;
import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CascadaNumericaService {

    private static final String NOMBRE_JUEGO = "Cascada Numérica";

    private static final int MAX_OPERACIONES = 20;
    private static final int DURACION_MAXIMA_SEGUNDOS = 120;

    private static final int NIVEL_MINIMO = 1;
    private static final int NIVEL_MAXIMO = 5;

    // Ajustado a pedido: los números caían muy rápido y no daba tiempo de leer
    // la operación y responder. Se sube el punto de partida y el piso mínimo
    // (aun en el nivel más difícil nunca cae más rápido que VELOCIDAD_MINIMA_MS).
    // Segundo ajuste: seguía sintiéndose rápido, se sube de nuevo.
    private static final int VELOCIDAD_INICIAL_MS = 8500;
    private static final int VELOCIDAD_MINIMA_MS = 4500;
    private static final int VELOCIDAD_MAXIMA_MS = 9500;

    private static final Set<String> TIPOS_VALIDOS =
            Set.of("ACIERTO", "ERROR", "OMISION");

    private final SesionJuegoRepository sesionJuegoRepository;
    private final JuegoRepository juegoRepository;
    private final NivelDificultadRepository nivelDificultadRepository;
    private final PerfilNinoRepository perfilNinoRepository;
    private final CascadaOperacionEventoRepository operacionRepository;
    private final MetricaRepository metricaRepository;
    private final NivelAsignadoRepository nivelAsignadoRepository;

    @Transactional
    public IniciarCascadaResponse iniciarSesion(
            IniciarSesionRequest request
    ) {
        PerfilNino perfil = perfilNinoRepository
                .findById(request.getPerfilId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No se encontró el perfil infantil"
                        )
                );

        if (Boolean.FALSE.equals(perfil.getActivo())) {
            throw new IllegalStateException(
                    "El perfil infantil se encuentra inactivo"
            );
        }

        Juego juego = juegoRepository
                .findByNombreIgnoreCase(NOMBRE_JUEGO)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Cascada Numérica no está registrada"
                        )
                );

        if (Boolean.FALSE.equals(juego.getActivo())) {
            throw new IllegalStateException(
                    "Cascada Numérica se encuentra desactivada"
            );
        }

        // El niño solo puede jugar el nivel que el docente/padre le fijó: si hay
        // un NivelAsignado, la sesión arranca directo ahí en vez de NIVEL_MINIMO,
        // y calcularAjuste/finalizarSesion lo mantienen fijo el resto de la sesión.
        Integer nivelBloqueado = obtenerNivelBloqueado(perfil.getId(), juego.getId());
        int nivelInicioSesion = nivelBloqueado != null ? nivelBloqueado : NIVEL_MINIMO;

        NivelDificultad nivelInicial =
                obtenerNivel(juego.getId(), nivelInicioSesion);

        SesionJuego sesion = SesionJuego.builder()
                .perfil(perfil)
                .juego(juego)
                .nivel(nivelInicial)
                .inicio(LocalDateTime.now())
                .fin(null)
                .puntaje(0)
                .completada(false)
                .build();

        SesionJuego sesionGuardada =
                sesionJuegoRepository.save(sesion);

        return IniciarCascadaResponse.builder()
                .sesionId(sesionGuardada.getId())
                .perfilId(perfil.getId())
                .juegoId(juego.getId())
                .nivelId(nivelInicial.getId())
                .nivelInicial(nivelInicioSesion)
                .velocidadCaidaMs(VELOCIDAD_INICIAL_MS)
                .maxOperaciones(MAX_OPERACIONES)
                .duracionMaximaSegundos(
                        DURACION_MAXIMA_SEGUNDOS
                )
                .build();
    }

    @Transactional
    public RegistrarOperacionResponse registrarOperacion(
            Integer sesionId,
            RegistrarOperacionRequest request
    ) {
        SesionJuego sesion = obtenerSesion(sesionId);

        validarSesionDisponible(sesion);
        validarOperacion(request);

        long cantidadActual =
                operacionRepository.countBySesionId(sesionId);

        if (cantidadActual >= MAX_OPERACIONES) {
            throw new IllegalStateException(
                    "La sesión ya alcanzó el máximo de 20 operaciones"
            );
        }

        if (request.getNumeroOperacion() != cantidadActual + 1) {
            throw new IllegalArgumentException(
                    "El número de operación no corresponde con la secuencia esperada"
            );
        }

        CascadaOperacionEvento evento =
                CascadaOperacionEvento.builder()
                        .sesion(sesion)
                        .numeroOperacion(
                                request.getNumeroOperacion()
                        )
                        .numero1(request.getNumero1())
                        .numero2(request.getNumero2())
                        .operador(
                                normalizarOperador(
                                        request.getOperador()
                                )
                        )
                        .resultadoCorrecto(
                                request.getResultadoCorrecto()
                        )
                        .respuestaSeleccionada(
                                request.getRespuestaSeleccionada()
                        )
                        .tipoResultado(
                                request.getTipoResultado().toUpperCase()
                        )
                        .tiempoRespuestaMs(
                                request.getTiempoRespuestaMs()
                        )
                        .velocidadCaidaMs(
                                request.getVelocidadCaidaMs()
                        )
                        .nivel(request.getNivel())
                        .build();

        CascadaOperacionEvento guardado =
                operacionRepository.save(evento);

        List<CascadaOperacionEvento> ultimas =
                operacionRepository
                        .findTop5BySesionIdOrderByNumeroOperacionDesc(
                                sesionId
                        );

        AjusteDificultad ajuste =
                calcularAjuste(
                        ultimas,
                        request.getNivel(),
                        request.getVelocidadCaidaMs(),
                        obtenerNivelBloqueado(sesion.getPerfil().getId(), sesion.getJuego().getId())
                );

        return RegistrarOperacionResponse.builder()
                .operacionId(guardado.getId())
                .operacionesRegistradas(
                        Math.toIntExact(cantidadActual + 1)
                )
                .nivelSugerido(ajuste.nivel())
                .velocidadSugeridaMs(ajuste.velocidadMs())
                .precisionUltimasOperaciones(
                        ajuste.precision()
                )
                .dificultadModificada(
                        ajuste.modificada()
                )
                .build();
    }

    @Transactional
    public CascadaResultadoResponse finalizarSesion(
            Integer sesionId,
            FinalizarCascadaRequest request
    ) {
        SesionJuego sesion = obtenerSesion(sesionId);

        // Nota: el frontend llama primero al endpoint genérico de finalizar
        // sesión (que ya marca completada=true) y justo después a este
        // endpoint dedicado con el detalle de Cascada Numérica. Antes esto
        // lanzaba una excepción si la llamada genérica llegaba primero
        // (carrera entre dos peticiones HTTP separadas), lo que podía perder
        // silenciosamente la Métrica detallada de Cascada. Ahora se procesa
        // de forma idempotente: la Métrica se actualiza (upsert) en vez de
        // duplicarse o perderse.
        List<CascadaOperacionEvento> operaciones =
                operacionRepository
                        .findBySesionIdOrderByNumeroOperacionAsc(
                                sesionId
                        );

        int aciertosCalculados =
                contarTipo(operaciones, "ACIERTO");

        int erroresCalculados =
                contarTipo(operaciones, "ERROR");

        int omisionesCalculadas =
                contarTipo(operaciones, "OMISION");

        int total = operaciones.size();

        double precision = total == 0
                ? 0
                : (aciertosCalculados * 100.0) / total;

        double tiempoPromedio =
                operaciones.stream()
                        .filter(op ->
                                op.getTiempoRespuestaMs() != null
                        )
                        .mapToLong(
                                CascadaOperacionEvento::
                                        getTiempoRespuestaMs
                        )
                        .average()
                        .orElse(0);

        double velocidadPromedio =
                operaciones.stream()
                        .mapToInt(
                                CascadaOperacionEvento::
                                        getVelocidadCaidaMs
                        )
                        .average()
                        .orElse(0);

        // Si hay un nivel bloqueado para este perfil+juego, el nivel final que
        // reporte el cliente se ignora — la sesión completa se jugó (y se
        // puntúa) en el nivel fijado por el docente/padre.
        Integer nivelBloqueado = obtenerNivelBloqueado(sesion.getPerfil().getId(), sesion.getJuego().getId());
        int nivelFinalEfectivo = nivelBloqueado != null ? nivelBloqueado : limitarNivel(request.getNivelFinal());

        int puntaje = calcularPuntaje(
                aciertosCalculados,
                erroresCalculados,
                omisionesCalculadas,
                request.getMaxCombo(),
                nivelFinalEfectivo
        );

        NivelDificultad nivelFinal = obtenerNivel(
                sesion.getJuego().getId(),
                nivelFinalEfectivo
        );

        sesion.setFin(LocalDateTime.now());
        sesion.setPuntaje(puntaje);
        sesion.setCompletada(true);
        sesion.setNivel(nivelFinal);

        sesionJuegoRepository.save(sesion);

        Metrica metrica = metricaRepository.findBySesionId(sesion.getId())
                .orElseGet(() -> Metrica.builder().sesion(sesion).build());
        metrica.setTiempoReaccionProm(decimal(tiempoPromedio));
        metrica.setPrecisionPct(decimal(precision));
        metrica.setErrores(erroresCalculados + omisionesCalculadas);
        metrica.setZonaFallo(determinarZonaFallo(erroresCalculados, omisionesCalculadas));

        metricaRepository.save(metrica);

        return CascadaResultadoResponse.builder()
                .sesionId(sesion.getId())
                .operacionesCompletadas(total)
                .aciertos(aciertosCalculados)
                .errores(erroresCalculados)
                .omisiones(omisionesCalculadas)
                .precisionPorcentaje(
                        redondear(precision)
                )
                .tiempoRespuestaPromedioMs(
                        redondear(tiempoPromedio)
                )
                .velocidadPromedioMs(
                        redondear(velocidadPromedio)
                )
                .maxCombo(request.getMaxCombo())
                .nivelFinal(
                        nivelFinalEfectivo
                )
                .puntaje(puntaje)
                .completada(true)
                .build();
    }

    @Transactional(readOnly = true)
    public List<CascadaOperacionEvento> obtenerOperaciones(
            Integer sesionId
    ) {
        obtenerSesion(sesionId);

        return operacionRepository
                .findBySesionIdOrderByNumeroOperacionAsc(
                        sesionId
                );
    }

    private SesionJuego obtenerSesion(Integer sesionId) {
        SesionJuego sesion = sesionJuegoRepository
                .findById(sesionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No se encontró la sesión"
                        )
                );

        if (
                sesion.getJuego() == null ||
                        !NOMBRE_JUEGO.equalsIgnoreCase(
                                sesion.getJuego().getNombre()
                        )
        ) {
            throw new IllegalArgumentException(
                    "La sesión no pertenece a Cascada Numérica"
            );
        }

        return sesion;
    }

    private void validarSesionDisponible(
            SesionJuego sesion
    ) {
        if (Boolean.TRUE.equals(sesion.getCompletada())) {
            throw new IllegalStateException(
                    "La sesión ya fue finalizada"
            );
        }

        long segundosTranscurridos = Duration.between(
                sesion.getInicio(),
                LocalDateTime.now()
        ).getSeconds();

        if (segundosTranscurridos >= DURACION_MAXIMA_SEGUNDOS) {
            throw new IllegalStateException(
                    "La sesión superó los cinco minutos"
            );
        }
    }

    private void validarOperacion(
            RegistrarOperacionRequest request
    ) {
        String tipo = request
                .getTipoResultado()
                .toUpperCase();

        if (!TIPOS_VALIDOS.contains(tipo)) {
            throw new IllegalArgumentException(
                    "tipoResultado debe ser ACIERTO, ERROR u OMISION"
            );
        }

        int resultadoEsperado = calcularResultado(
                request.getNumero1(),
                request.getNumero2(),
                request.getOperador()
        );

        if (
                resultadoEsperado !=
                        request.getResultadoCorrecto()
        ) {
            throw new IllegalArgumentException(
                    "El resultado correcto no coincide con la operación"
            );
        }

        if (
                "ACIERTO".equals(tipo) &&
                        !request.getResultadoCorrecto()
                                .equals(
                                        request.getRespuestaSeleccionada()
                                )
        ) {
            throw new IllegalArgumentException(
                    "Una respuesta marcada como ACIERTO debe coincidir con el resultado"
            );
        }

        if (
                "ERROR".equals(tipo) &&
                        (
                                request.getRespuestaSeleccionada() == null ||
                                        request.getResultadoCorrecto()
                                                .equals(
                                                        request.getRespuestaSeleccionada()
                                                )
                        )
        ) {
            throw new IllegalArgumentException(
                    "Una respuesta marcada como ERROR debe ser incorrecta"
            );
        }

        if (
                "OMISION".equals(tipo) &&
                        request.getRespuestaSeleccionada() != null
        ) {
            throw new IllegalArgumentException(
                    "Una OMISION no debe contener una respuesta seleccionada"
            );
        }
    }

    private int calcularResultado(
            int numero1,
            int numero2,
            String operador
    ) {
        return switch (normalizarOperador(operador)) {
            case "SUMA" -> numero1 + numero2;
            case "RESTA" -> numero1 - numero2;
            case "MULTIPLICACION" -> numero1 * numero2;
            default -> throw new IllegalArgumentException(
                    "Operador no permitido"
            );
        };
    }

    private String normalizarOperador(String operador) {
        return switch (operador.trim().toUpperCase()) {
            case "+", "SUMA" -> "SUMA";
            case "-", "RESTA" -> "RESTA";
            case "×", "X", "*", "MULTIPLICACION" ->
                    "MULTIPLICACION";
            default -> throw new IllegalArgumentException(
                    "Operador no permitido: " + operador
            );
        };
    }

    private AjusteDificultad calcularAjuste(
            List<CascadaOperacionEvento> ultimas,
            int nivelActual,
            int velocidadActual,
            Integer nivelBloqueado
    ) {
        // Si el docente/padre fijó un nivel para este perfil+juego, el ajuste
        // adaptativo queda desactivado: el nivel se mantiene siempre en el
        // valor bloqueado (nunca sube ni baja durante la sesión).
        if (nivelBloqueado != null) {
            double precisionActual = calcularPrecision(ultimas);
            return new AjusteDificultad(
                    nivelBloqueado,
                    limitarVelocidad(velocidadActual),
                    redondear(precisionActual),
                    false
            );
        }

        if (ultimas.size() < 5) {
            double precisionParcial =
                    calcularPrecision(ultimas);

            return new AjusteDificultad(
                    limitarNivel(nivelActual),
                    limitarVelocidad(velocidadActual),
                    precisionParcial,
                    false
            );
        }

        double precision =
                calcularPrecision(ultimas);

        int nuevoNivel = nivelActual;
        int nuevaVelocidad = velocidadActual;
        boolean modificada = false;

        if (precision >= 90) {
            nuevoNivel = Math.min(
                    NIVEL_MAXIMO,
                    nivelActual + 1
            );

            nuevaVelocidad = Math.max(
                    VELOCIDAD_MINIMA_MS,
                    velocidadActual - 250
            );

            modificada =
                    nuevoNivel != nivelActual ||
                            nuevaVelocidad != velocidadActual;
        } else if (precision <= 40) {
            nuevoNivel = Math.max(
                    NIVEL_MINIMO,
                    nivelActual - 1
            );

            nuevaVelocidad = Math.min(
                    VELOCIDAD_MAXIMA_MS,
                    velocidadActual + 250
            );

            modificada =
                    nuevoNivel != nivelActual ||
                            nuevaVelocidad != velocidadActual;
        }

        return new AjusteDificultad(
                nuevoNivel,
                nuevaVelocidad,
                redondear(precision),
                modificada
        );
    }

    private double calcularPrecision(
            List<CascadaOperacionEvento> operaciones
    ) {
        if (operaciones.isEmpty()) {
            return 0;
        }

        long aciertos = operaciones.stream()
                .filter(op ->
                        "ACIERTO".equals(
                                op.getTipoResultado()
                        )
                )
                .count();

        return aciertos * 100.0 /
                operaciones.size();
    }

    private int contarTipo(
            List<CascadaOperacionEvento> operaciones,
            String tipo
    ) {
        return Math.toIntExact(
                operaciones.stream()
                        .filter(op ->
                                tipo.equals(
                                        op.getTipoResultado()
                                )
                        )
                        .count()
        );
    }

    private int calcularPuntaje(
            int aciertos,
            int errores,
            int omisiones,
            int maxCombo,
            int nivelFinal
    ) {
        int puntaje =
                aciertos * 100 +
                        maxCombo * 20 +
                        limitarNivel(nivelFinal) * 50 -
                        errores * 25 -
                        omisiones * 15;

        return Math.max(0, puntaje);
    }

    private String determinarZonaFallo(
            int errores,
            int omisiones
    ) {
        if (errores == 0 && omisiones == 0) {
            return "SIN_FALLOS_RELEVANTES";
        }

        if (omisiones > errores) {
            return "VELOCIDAD_DE_RESPUESTA";
        }

        if (errores > omisiones) {
            return "CALCULO_MENTAL";
        }

        return "CALCULO_Y_VELOCIDAD";
    }

    private NivelDificultad obtenerNivel(
            Integer juegoId,
            int nivel
    ) {
        int nivelLimitado = limitarNivel(nivel);

        String nombreNivel = switch (nivelLimitado) {
            case 1, 2 -> "FACIL";
            case 3 -> "MEDIO";
            case 4, 5 -> "DIFICIL";
            default -> "FACIL";
        };

        return nivelDificultadRepository
                .findByJuegoIdAndNivel(
                        juegoId,
                        nombreNivel
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No se encontró el nivel " +
                                        nombreNivel +
                                        " de Cascada Numérica"
                        )
                );
    }

    private int limitarNivel(int nivel) {
        return Math.max(
                NIVEL_MINIMO,
                Math.min(NIVEL_MAXIMO, nivel)
        );
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

    private int limitarVelocidad(int velocidad) {
        return Math.max(
                VELOCIDAD_MINIMA_MS,
                Math.min(
                        VELOCIDAD_MAXIMA_MS,
                        velocidad
                )
        );
    }

    private BigDecimal decimal(double valor) {
        return BigDecimal.valueOf(valor)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private double redondear(double valor) {
        return decimal(valor).doubleValue();
    }

    private record AjusteDificultad(
            int nivel,
            int velocidadMs,
            double precision,
            boolean modificada
    ) {
    }
}