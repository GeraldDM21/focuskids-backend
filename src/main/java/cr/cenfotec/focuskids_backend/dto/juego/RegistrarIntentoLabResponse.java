package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrarIntentoLabResponse {

    private Integer intentoId;

    private Integer intentosRegistrados;

    private String nivelSugerido;

    private Integer ingredientesSugeridos;

    private Boolean dificultadModificada;
}