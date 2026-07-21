package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FinalizarCascadaRequest {

    @NotNull
    @Min(0)
    private Integer aciertos;

    @NotNull
    @Min(0)
    private Integer errores;

    @NotNull
    @Min(0)
    private Integer omisiones;

    @NotNull
    @Min(0)
    private Integer maxCombo;

    @NotNull
    @Min(0)
    private Long duracionTotalMs;

    @NotNull
    @Min(1)
    private Integer nivelFinal;
}