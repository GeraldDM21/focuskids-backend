package cr.cenfotec.focuskids_backend.dto.juego;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// RF-29: se envía una sola vez, al terminar todas las rondas de laberintos de la sesión.
@Data
public class FinalizarLaberintoRequest {

    @NotNull
    @Min(1)
    private Integer rondasCompletadas;

    @NotNull
    @Min(1)
    private Integer pasosUsadosTotal;

    @NotNull
    @Min(1)
    private Integer pasosOptimosTotal;

    @NotNull
    @Min(0)
    private Long tiempoResolucionMsTotal;

    @NotNull
    @Min(0)
    private Integer callejonesSinSalidaVisitadosTotal;

    @NotNull
    private Boolean planificoEnPrimerMovimiento; // CA-03

    @NotNull
    @Min(1)
    private Integer nivelMaximoAlcanzado; // 1..5 (CA-02/CA-06)
}
