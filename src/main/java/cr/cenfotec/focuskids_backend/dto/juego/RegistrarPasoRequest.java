package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrarPasoRequest {

    @NotNull
    @Min(1)
    private Integer numeroPaso;

    @NotBlank
    private String direccion; // ARRIBA | ABAJO | IZQUIERDA | DERECHA

    @NotNull
    private Integer posicionX;

    @NotNull
    private Integer posicionY;

    @NotNull
    private Boolean esCallejonSinSalida;

    @NotNull
    @Min(0)
    private Long tiempoDesdeInicioMs;

    @NotNull
    @Min(1)
    private Integer nivel;
}
