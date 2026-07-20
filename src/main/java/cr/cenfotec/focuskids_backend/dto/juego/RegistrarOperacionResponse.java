package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrarOperacionResponse {

    private Integer operacionId;
    private Integer operacionesRegistradas;

    private Integer nivelSugerido;
    private Integer velocidadSugeridaMs;

    private Double precisionUltimasOperaciones;
    private Boolean dificultadModificada;
}