package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IniciarLabRequest {

    @NotNull
    private Integer perfilId;

    @NotBlank
    private String nivel;
}