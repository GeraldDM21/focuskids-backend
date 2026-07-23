package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IniciarLabResponse {

    private Integer sesionId;

    private Integer perfilId;

    private Integer juegoId;

    private Integer nivelId;

    private String nivelSeleccionado;

    private Integer ingredientesIniciales;

    private Integer experimentosObjetivo;
}