package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IniciarLaberintoRequest {

    @NotNull(message = "El perfilId es obligatorio")
    private Integer perfilId;
}
