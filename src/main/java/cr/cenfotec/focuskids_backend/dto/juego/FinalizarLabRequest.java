package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FinalizarLabRequest {

    @NotNull
    @Min(0)
    private Integer experimentosCompletados;

    @NotNull
    @Min(0)
    private Integer hipotesisCorrectas;

    @NotNull
    @Min(0)
    private Integer hipotesisIncorrectas;

    @NotNull
    @Min(0)
    private Integer intentosTotales;

    @NotNull
    @Min(0)
    private Long tiempoTotalMs;

    @NotBlank
    private String nivelFinal;
}