package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaratonResultadoResponse {

    private Integer sesionId;

    // ── Desempeño por tarea (datos de salida pedidos en el RF) ────────────
    private Double precisionIndividualAPorcentaje;
    private Double precisionIndividualBPorcentaje;
    private Double precisionDualAPorcentaje;
    private Double precisionDualBPorcentaje;

    private Double tiempoRespuestaIndividualAMs;
    private Double tiempoRespuestaIndividualBMs;
    private Double tiempoRespuestaDualAMs;
    private Double tiempoRespuestaDualBMs;

    // ── CA-04: índice de costo dual por tarea (diferencia individual vs dual) ─
    private Double costoDualAPorcentaje;
    private Double costoDualBPorcentaje;

    // Índice de interferencia entre tareas = promedio de ambos costos duales
    private Double indiceInterferenciaPorcentaje;

    // Carga cognitiva estimada (0-100) y su categoría amigable
    private Double cargaCognitivaEstimada;
    private String cargaCognitivaCategoria; // BAJA | MEDIA | ALTA

    // CA-06: cuál tarea se degrada más = área cognitiva más débil
    private String tareaMasDebil; // A | B | NINGUNA

    private Integer rondasDualesCompletadas;
    private Integer aciertosDualesTotales;
    private Integer erroresDualesTotales;

    private String nivelFinal;
    private String nivelSugerido;
    private Integer puntaje;
    private Boolean completada;
}
