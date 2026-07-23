package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RegistrarIntentoLabRequest {

    @NotNull
    @Min(1)
    private Integer numeroExperimento;

    @NotBlank
    private String experimentoCodigo;

    @NotEmpty
    @Size(min = 2, max = 4)
    private List<@NotBlank String> ingredientes;

    @NotNull
    private Boolean exitoso;

    @NotNull
    @Min(0)
    private Long tiempoIntentoMs;

    @NotNull
    @Min(1)
    private Integer intentosAcumuladosExperimento;

    @NotBlank
    private String nivel;
}