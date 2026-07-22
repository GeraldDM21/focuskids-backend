package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IniciarLaberintoResponse {

    private Integer sesionId;
    private Integer perfilId;
    private Integer juegoId;
    private Integer nivelId;

    private Integer nivelInicial;        // 1..5 (CA-02)
    private Integer tamanoMapa;           // celdas por lado, ej. 5 (CA-02)
    private Boolean obstaculosDinamicos;  // true desde nivel 3 (CA-04)
    private Integer tiempoDespliegueMs;   // 3000 (CA-01)
}
