package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.juego.FinalizarLabRequest;
import cr.cenfotec.focuskids_backend.dto.juego.IniciarLabRequest;
import cr.cenfotec.focuskids_backend.dto.juego.IniciarLabResponse;
import cr.cenfotec.focuskids_backend.dto.juego.LabResultadoResponse;
import cr.cenfotec.focuskids_backend.dto.juego.RegistrarIntentoLabRequest;
import cr.cenfotec.focuskids_backend.dto.juego.RegistrarIntentoLabResponse;
import cr.cenfotec.focuskids_backend.model.Juego;
import cr.cenfotec.focuskids_backend.model.LabCienciasIntento;
import cr.cenfotec.focuskids_backend.model.Metrica;
import cr.cenfotec.focuskids_backend.model.NivelDificultad;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.SesionJuego;
import cr.cenfotec.focuskids_backend.repository.JuegoRepository;
import cr.cenfotec.focuskids_backend.repository.LabCienciasIntentoRepository;
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

@Service
@RequiredArgsConstructor
public class LabCienciasService {

    private static final String NOMBRE_JUEGO = "Lab de Ciencias";

    private static final List<String> NIVELES = List.of(
            "FACIL",
            "MEDIO",
            "DIFICIL",
            "EXPERTO"
    );

    private final SesionJuegoRepository sesionRepository;

    private final JuegoRepository juegoRepository;

    private final NivelDificultadRepository nivelRepository;

    private final PerfilNinoRepository perfilRepository;

    private final LabCienciasIntentoRepository intentoRepository;

    private final MetricaRepository metricaRepository;

    @Transactional
    public IniciarLabResponse iniciarSesion(
            IniciarLabRequest request
    ) {
        String nivel = normalizarNivel(request.getNivel());

        PerfilNino perfil = perfilRepository
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
                                "Lab de Ciencias no está registrado"
                        )
                );

        if (Boolean.FALSE.equals(juego.getActivo())) {
            throw new IllegalStateException(
                    "Lab de Ciencias está desactivado"
            );
        }

        NivelDificultad nivelBd = obtenerNivelBd(
                juego.getId(),
                nivel
        );

        SesionJuego sesion = SesionJuego.builder()
                .perfil(perfil)
                .juego(juego)
                .nivel(nivelBd)
                .inicio(LocalDateTime.now())
                .puntaje(0)
                .completada(false)
                .build();

        sesion = sesionRepository.save(sesion);

        return IniciarLabResponse.builder()
                .sesionId(sesion.getId())
                .perfilId(perfil.getId())
                .juegoId(juego.getId())
                .nivelId(nivelBd.getId())
                .nivelSeleccionado(nivel)
                .ingredientesIniciales(
                        ingredientesPorNivel(nivel)
                )
                .experimentosObjetivo(
                        experimentosPorNivel(nivel)
                )
                .build();
    }

    @Transactional
    public RegistrarIntentoLabResponse registrarIntento(
            Integer sesionId,
            RegistrarIntentoLabRequest request
    ) {
        SesionJuego sesion =
                obtenerSesion(sesionId);

        if (
                Boolean.TRUE.equals(
                        sesion.getCompletada()
                )
        ) {
            throw new IllegalStateException(
                    "La sesión ya fue finalizada"
            );
        }

        String nivel =
                normalizarNivel(
                        request.getNivel()
                );

        List<String> ingredientes =
                request.getIngredientes()
                        .stream()
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .distinct()
                        .toList();

        int cantidadEsperada =
                cantidadCombinacionPorNivel(
                        nivel
                );

        if (
                ingredientes.size()
                        != cantidadEsperada
        ) {
            throw new IllegalArgumentException(
                    "El nivel "
                            + nivel
                            + " requiere "
                            + cantidadEsperada
                            + " ingredientes"
            );
        }

        String ingrediente1 =
                ingredientes.get(0);

        String ingrediente2 =
                ingredientes.get(1);

        String ingredientesCompletos =
                String.join(
                        ",",
                        ingredientes
                );

        LabCienciasIntento intento =
                LabCienciasIntento.builder()
                        .sesion(sesion)
                        .numeroExperimento(
                                request.getNumeroExperimento()
                        )
                        .experimentoCodigo(
                                request.getExperimentoCodigo()
                        )
                        .ingrediente1(
                                ingrediente1
                        )
                        .ingrediente2(
                                ingrediente2
                        )
                        .ingredientesSeleccionados(
                                ingredientesCompletos
                        )
                        .exitoso(
                                request.getExitoso()
                        )
                        .tiempoIntentoMs(
                                request.getTiempoIntentoMs()
                        )
                        .intentosAcumuladosExperimento(
                                request
                                        .getIntentosAcumuladosExperimento()
                        )
                        .nivel(nivel)
                        .build();

        intento =
                intentoRepository.save(intento);

        List<LabCienciasIntento> intentos =
                intentoRepository
                        .findBySesionIdOrderByIdAsc(
                                sesionId
                        );

        Ajuste ajuste =
                calcularAjuste(
                        intentos,
                        nivel
                );

        return RegistrarIntentoLabResponse
                .builder()
                .intentoId(
                        intento.getId()
                )
                .intentosRegistrados(
                        intentos.size()
                )
                .nivelSugerido(
                        ajuste.nivel()
                )
                .ingredientesSugeridos(
                        ingredientesPorNivel(
                                ajuste.nivel()
                        )
                )
                .dificultadModificada(
                        !ajuste.nivel()
                                .equals(nivel)
                )
                .build();
    }

    @Transactional
    public LabResultadoResponse finalizarSesion(
            Integer sesionId,
            FinalizarLabRequest request
    ) {
        SesionJuego sesion =
                obtenerSesion(sesionId);

        String nivelFinal =
                normalizarNivel(
                        request.getNivelFinal()
                );

        List<LabCienciasIntento> intentos =
                intentoRepository
                        .findBySesionIdOrderByIdAsc(
                                sesionId
                        );

        int total =
                intentos.size();

        long correctos =
                intentos.stream()
                        .filter(intento ->
                                Boolean.TRUE.equals(
                                        intento.getExitoso()
                                )
                        )
                        .count();

        long incorrectos =
                total - correctos;

        double precision =
                total == 0
                        ? 0
                        : correctos
                        * 100.0
                        / total;

        double promedio =
                intentos.stream()
                        .filter(intento ->
                                Boolean.TRUE.equals(
                                        intento.getExitoso()
                                )
                        )
                        .mapToLong(
                                LabCienciasIntento
                                        ::getTiempoIntentoMs
                        )
                        .average()
                        .orElse(0);

        int experimentosCompletados =
                Math.max(
                        0,
                        request.getExperimentosCompletados()
                );

        int puntaje =
                Math.max(
                        0,
                        experimentosCompletados * 500
                                - (int) incorrectos * 80
                );

        Ajuste ajuste =
                calcularAjuste(
                        intentos,
                        nivelFinal
                );

        /*
         * La finalización pasa a ser idempotente.
         * Si ya estaba terminada, vuelve a producir
         * el resumen sin lanzar un 400.
         */
        if (
                !Boolean.TRUE.equals(
                        sesion.getCompletada()
                )
        ) {
            sesion.setFin(
                    LocalDateTime.now()
            );

            sesion.setPuntaje(
                    puntaje
            );

            sesion.setCompletada(
                    true
            );

            sesionRepository.save(
                    sesion
            );
        }

        Metrica metrica =
                metricaRepository
                        .findBySesionId(
                                sesionId
                        )
                        .orElseGet(() ->
                                Metrica.builder()
                                        .sesion(sesion)
                                        .build()
                        );

        metrica.setTiempoReaccionProm(
                BigDecimal.valueOf(
                        promedio
                ).setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        metrica.setPrecisionPct(
                BigDecimal.valueOf(
                        precision
                ).setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        metrica.setErrores(
                (int) incorrectos
        );

        metrica.setZonaFallo(
                incorrectos > 0
                        ? "HIPOTESIS_INCORRECTA"
                        : "SIN_FALLOS"
        );

        metricaRepository.save(
                metrica
        );

        return LabResultadoResponse
                .builder()
                .sesionId(sesionId)
                .experimentosCompletados(
                        experimentosCompletados
                )
                .hipotesisCorrectas(
                        (int) correctos
                )
                .hipotesisIncorrectas(
                        (int) incorrectos
                )
                .intentosTotales(total)
                .precisionPorcentaje(
                        redondear(
                                precision
                        )
                )
                .tiempoDescubrimientoPromedioMs(
                        redondear(
                                promedio
                        )
                )
                .nivelFinal(
                        nivelFinal
                )
                .nivelSugerido(
                        ajuste.nivel()
                )
                .puntaje(
                        puntaje
                )
                .completada(
                        true
                )
                .build();
    }

    @Transactional(readOnly = true)
    public List<LabCienciasIntento> obtenerIntentos(
            Integer sesionId
    ) {
        obtenerSesion(sesionId);

        return intentoRepository
                .findBySesionIdOrderByIdAsc(sesionId);
    }

    private SesionJuego obtenerSesion(Integer id) {
        return sesionRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No se encontró la sesión"
                        )
                );
    }

    private String normalizarNivel(String nivel) {
        String nivelNormalizado = nivel == null
                ? ""
                : nivel.trim().toUpperCase();

        if (!NIVELES.contains(nivelNormalizado)) {
            throw new IllegalArgumentException(
                    "Nivel no válido"
            );
        }

        return nivelNormalizado;
    }

    private NivelDificultad obtenerNivelBd(
            Integer juegoId,
            String nivel
    ) {
        /*
         * La base de datos actual tiene:
         * FACIL, MEDIO y DIFICIL.
         *
         * EXPERTO utiliza el registro DIFICIL,
         * pero conserva EXPERTO en los eventos
         * particulares de Lab de Ciencias.
         */
        String nivelPersistido = nivel.equals("EXPERTO")
                ? "DIFICIL"
                : nivel;

        return nivelRepository
                .findByJuegoIdAndNivel(
                        juegoId,
                        nivelPersistido
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No existe el nivel "
                                        + nivelPersistido
                                        + " para Lab de Ciencias"
                        )
                );
    }

    private int ingredientesPorNivel(String nivel) {
        return switch (nivel) {
            case "FACIL" -> 4;
            case "MEDIO" -> 5;
            default -> 6;
        };
    }

    private int cantidadCombinacionPorNivel(
            String nivel
    ) {
        return switch (nivel) {
            case "FACIL" -> 2;
            case "MEDIO" -> 3;
            case "DIFICIL" -> 3;
            case "EXPERTO" -> 4;
            default -> 2;
        };
    }

    private int experimentosPorNivel(String nivel) {
        return switch (nivel) {
            case "FACIL" -> 2;
            case "EXPERTO" -> 4;
            default -> 3;
        };
    }

    private Ajuste calcularAjuste(
            List<LabCienciasIntento> todos,
            String nivelActual
    ) {
        /*
         * No se cambia la dificultad hasta tener
         * al menos tres intentos.
         */
        if (todos.size() < 3) {
            return new Ajuste(nivelActual);
        }

        /*
         * Se analizan solamente los seis intentos
         * más recientes.
         */
        List<LabCienciasIntento> ultimos =
                todos.subList(
                        Math.max(0, todos.size() - 6),
                        todos.size()
                );

        long aciertos = ultimos.stream()
                .filter(intento ->
                        Boolean.TRUE.equals(intento.getExitoso())
                )
                .count();

        double precision =
                aciertos / (double) ultimos.size();

        int indiceActual =
                NIVELES.indexOf(nivelActual);

        int nuevoIndice = indiceActual;

        if (
                precision >= 0.70
                        && indiceActual < NIVELES.size() - 1
        ) {
            nuevoIndice++;
        } else if (
                precision < 0.35
                        && indiceActual > 0
        ) {
            nuevoIndice--;
        }

        return new Ajuste(
                NIVELES.get(nuevoIndice)
        );
    }

    private double redondear(double valor) {
        return BigDecimal.valueOf(valor)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                )
                .doubleValue();
    }

    private record Ajuste(String nivel) {
    }
}