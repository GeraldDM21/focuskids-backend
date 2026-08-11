package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FinalizarMaratonRequest {

    @NotBlank
    private String nivelFinal;

    @NotNull
    @Min(0)
    private Long tiempoTotalMs;
}
