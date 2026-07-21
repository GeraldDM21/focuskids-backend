package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IniciarCascadaResponse {

    private Integer sesionId;
    private Integer perfilId;
    private Integer juegoId;
    private Integer nivelId;

    private Integer nivelInicial;
    private Integer velocidadCaidaMs;

    private Integer maxOperaciones;
    private Integer duracionMaximaSegundos;
}