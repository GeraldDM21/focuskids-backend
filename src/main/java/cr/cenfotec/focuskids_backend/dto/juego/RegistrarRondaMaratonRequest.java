package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrarRondaMaratonRequest {

    @NotNull
    @Min(1)
    private Integer numeroRonda;

    // CALIBRACION_A | CALIBRACION_B | DUAL
    @NotBlank
    private String fase;

    private Boolean tareaARespondida;
    private Boolean tareaACorrecta;

    @Min(0)
    private Long tareaATiempoRespuestaMs;

    private Boolean tareaBRespondida;
    private Boolean tareaBCorrecta;

    @Min(0)
    private Long tareaBTiempoRespuestaMs;

    @NotBlank
    private String nivel;
}
