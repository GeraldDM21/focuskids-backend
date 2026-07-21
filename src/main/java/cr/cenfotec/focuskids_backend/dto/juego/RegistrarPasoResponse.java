package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrarPasoResponse {

    private Integer pasoId;
    private Integer pasosRegistrados;
    private Integer callejonesSinSalidaHastaAhora;
}
