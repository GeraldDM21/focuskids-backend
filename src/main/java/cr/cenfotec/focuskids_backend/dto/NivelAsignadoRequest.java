package cr.cenfotec.focuskids_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NivelAsignadoRequest {

    // FACIL | MEDIO | DIFICIL
    @NotBlank
    private String nivel;
}
