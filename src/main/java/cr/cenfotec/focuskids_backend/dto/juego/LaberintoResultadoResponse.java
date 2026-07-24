package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LaberintoResultadoResponse {

    private Integer sesionId;

    private Integer rondasCompletadas;
    private Integer pasosUsadosTotal;
    private Integer pasosOptimosTotal;
    private Double porcentajeEficiencia; // CA-05

    private Long tiempoResolucionMsTotal;
    private Integer callejonesSinSalidaVisitadosTotal;
    private Boolean planificoEnPrimerMovimiento;

    private Integer nivelMaximoAlcanzado;
    private Integer puntaje;

    private Boolean completada;
}
