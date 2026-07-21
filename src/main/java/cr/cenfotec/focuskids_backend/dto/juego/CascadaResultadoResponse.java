package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CascadaResultadoResponse {

    private Integer sesionId;

    private Integer operacionesCompletadas;

    private Integer aciertos;
    private Integer errores;
    private Integer omisiones;

    private Double precisionPorcentaje;
    private Double tiempoRespuestaPromedioMs;
    private Double velocidadPromedioMs;

    private Integer maxCombo;
    private Integer nivelFinal;
    private Integer puntaje;

    private Boolean completada;
}