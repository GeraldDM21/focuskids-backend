package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.IaEvaluacionSesionRepository;
import cr.cenfotec.focuskids_backend.repository.SesionJuegoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Motor de IA — Historia: análisis de tendencias de sesiones.
 *
 * CA-01: exige mínimo 3 sesiones válidas del mismo niño+juego+nivel.
 * CA-02: pendiente de regresión lineal sobre porcentaje_aciertos de las
 *        últimas 5 sesiones válidas, clasificada en MEJORA / ESTANCAMIENTO
 *        / REGRESION.
 * CA-04: se ejecuta en un hilo separado (no bloquea al llamador).
 */
@Service
@RequiredArgsConstructor
public class IaEvaluacionService {

    private static final Logger log = LoggerFactory.getLogger(IaEvaluacionService.class);

    private static final int MIN_SESIONES = 3;
    private static final int VENTANA_MAX = 5;
    private static final BigDecimal UMBRAL_PENDIENTE = new BigDecimal("3.0");

    private final SesionJuegoRepository sesionJuegoRepository;
    private final IaEvaluacionSesionRepository iaEvaluacionSesionRepository;

    /**
     * CA-04: análisis asíncrono disparado tras finalizar una sesión.
     * No lanza excepciones hacia el llamador: cualquier fallo queda
     * registrado en logs para no afectar el flujo principal de la sesión.
     */
    @Async("iaEvaluacionExecutor")
    public void evaluarAsync(Integer perfilId, Integer juegoId, Integer nivelId) {
        try {
            evaluar(perfilId, juegoId, nivelId);
        } catch (Exception ex) {
            log.error("Motor de IA: fallo evaluando tendencia (perfilId={}, juegoId={}, nivelId={}): {}",
                    perfilId, juegoId, nivelId, ex.getMessage(), ex);
        }
    }

    // Nota: sin @Transactional propio — este método es invocado desde
    // evaluarAsync() dentro de la misma clase (self-invocation), por lo que
    // el proxy AOP de Spring no interceptaría la anotación. Cada llamada a
    // los repositorios ya maneja su propia transacción (comportamiento por
    // defecto de Spring Data JPA), lo cual es suficiente aquí: solo hay una
    // lectura y una escritura independientes, sin necesidad de atomicidad conjunta.
    public void evaluar(Integer perfilId, Integer juegoId, Integer nivelId) {
        List<SesionJuego> historico = sesionJuegoRepository
                .findByPerfilIdAndJuegoIdAndNivelIdAndSesionValidaTrueOrderByFinAsc(perfilId, juegoId, nivelId);

        // CA-01: mínimo 3 sesiones válidas antes de emitir cualquier evaluación.
        if (historico.size() < MIN_SESIONES) {
            log.debug("Motor de IA: sólo {} sesión(es) válida(s) (perfilId={}, juegoId={}, nivelId={}); se requieren {}",
                    historico.size(), perfilId, juegoId, nivelId, MIN_SESIONES);
            return;
        }

        // CA-02: se toman las últimas 5 (o menos si aún no hay 5).
        List<SesionJuego> ventana = historico.size() > VENTANA_MAX
                ? historico.subList(historico.size() - VENTANA_MAX, historico.size())
                : historico;

        RegresionResultado regresion = calcularRegresionLineal(ventana);
        TendenciaCognitiva tendencia = clasificarTendencia(regresion.pendiente);
        BigDecimal confianza = calcularConfianza(regresion.rCuadrado, ventana.size());

        SesionJuego ultimaSesion = ventana.get(ventana.size() - 1);

        IaEvaluacionSesion evaluacion = IaEvaluacionSesion.builder()
                .ninoPerfil(ultimaSesion.getPerfil())
                .juego(ultimaSesion.getJuego())
                .nivel(ultimaSesion.getNivel().getNivel())
                .tendencia(tendencia)
                .confianza(confianza)
                .sesionesAnalizadas(ventana.size())
                .fechaEvaluacion(LocalDateTime.now())
                .build();

        iaEvaluacionSesionRepository.save(evaluacion);

        log.info("Motor de IA: evaluación guardada (perfilId={}, juegoId={}, nivel={}, tendencia={}, confianza={}, n={})",
                perfilId, juegoId, evaluacion.getNivel(), tendencia, confianza, ventana.size());
    }

    // ── CA-02: regresión lineal simple (mínimos cuadrados) ──────────────────
    // x = índice de sesión dentro de la ventana (1..n, orden cronológico)
    // y = porcentaje_aciertos de cada sesión
    private RegresionResultado calcularRegresionLineal(List<SesionJuego> sesiones) {
        int n = sesiones.size();
        double[] x = new double[n];
        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            x[i] = i + 1;
            BigDecimal aciertos = sesiones.get(i).getPorcentajeAciertos();
            y[i] = aciertos != null ? aciertos.doubleValue() : 0.0;
        }

        double xMedia = promedio(x);
        double yMedia = promedio(y);

        double numerador = 0.0;
        double denominador = 0.0;
        for (int i = 0; i < n; i++) {
            numerador += (x[i] - xMedia) * (y[i] - yMedia);
            denominador += (x[i] - xMedia) * (x[i] - xMedia);
        }

        // Todas las x son distintas (1..n) por construcción, así que
        // denominador == 0 sólo puede pasar si n == 1 (no ocurre: MIN_SESIONES = 3).
        double pendiente = denominador != 0 ? numerador / denominador : 0.0;
        double intercepto = yMedia - pendiente * xMedia;

        // R² — qué tan bien se ajusta la recta a los datos reales.
        double ssTot = 0.0;
        double ssRes = 0.0;
        for (int i = 0; i < n; i++) {
            double yPred = pendiente * x[i] + intercepto;
            ssRes += Math.pow(y[i] - yPred, 2);
            ssTot += Math.pow(y[i] - yMedia, 2);
        }
        double rCuadrado = ssTot != 0 ? Math.max(0.0, 1 - (ssRes / ssTot)) : 1.0;

        return new RegresionResultado(pendiente, rCuadrado);
    }

    private double promedio(double[] valores) {
        double suma = 0.0;
        for (double v : valores) suma += v;
        return suma / valores.length;
    }

    // CA-02: clasificación de la pendiente
    private TendenciaCognitiva clasificarTendencia(double pendiente) {
        if (pendiente > UMBRAL_PENDIENTE.doubleValue()) {
            return TendenciaCognitiva.MEJORA;
        } else if (pendiente < -UMBRAL_PENDIENTE.doubleValue()) {
            return TendenciaCognitiva.REGRESION;
        }
        return TendenciaCognitiva.ESTANCAMIENTO;
    }

    /**
     * Confianza (0.0-1.0): combina la calidad del ajuste (R²) con la
     * cantidad de sesiones analizadas respecto a la ventana ideal (5).
     * Ambos factores pesan igual.
     */
    private BigDecimal calcularConfianza(double rCuadrado, int sesionesAnalizadas) {
        double factorMuestra = Math.min(1.0, sesionesAnalizadas / (double) VENTANA_MAX);
        double confianza = (rCuadrado * 0.5) + (factorMuestra * 0.5);
        confianza = Math.max(0.0, Math.min(1.0, confianza));
        return BigDecimal.valueOf(confianza).setScale(2, RoundingMode.HALF_UP);
    }

    private record RegresionResultado(double pendiente, double rCuadrado) {
    }
}
