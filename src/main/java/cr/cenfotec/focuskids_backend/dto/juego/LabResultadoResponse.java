package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LabResultadoResponse {

    private Integer sesionId;

    private Integer experimentosCompletados;

    private Integer hipotesisCorrectas;

    private Integer hipotesisIncorrectas;

    private Integer intentosTotales;

    private Double precisionPorcentaje;

    private Double tiempoDescubrimientoPromedioMs;

    private String nivelFinal;

    private String nivelSugerido;

    private Integer puntaje;

    private Boolean completada;
}