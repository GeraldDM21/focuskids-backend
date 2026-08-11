package cr.cenfotec.focuskids_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Parámetros de dificultad que el administrador puede modificar (CA-02).
 *
 * Rangos válidos:
 *   velocidadEstimulos : 500 – 5000 ms
 *   cantidadElementos  : 2   – 12
 *   tiempoLimite       : 5   – 60  s
 *   numRondas          : 5   – 30
 *
 * Si algún valor está fuera de rango el backend responde HTTP 400 con detalle.
 */
@Data
public class NivelConfigRequest {

    @NotNull(message = "velocidadEstimulos es requerido")
    @Min(value = 500,  message = "velocidadEstimulos mínimo es 500 ms")
    @Max(value = 5000, message = "velocidadEstimulos máximo es 5000 ms")
    private Integer velocidadEstimulos;

    @NotNull(message = "cantidadElementos es requerido")
    @Min(value = 2,  message = "cantidadElementos mínimo es 2")
    @Max(value = 12, message = "cantidadElementos máximo es 12")
    private Integer cantidadElementos;

    @NotNull(message = "tiempoLimite es requerido")
    @Min(value = 5,  message = "tiempoLimite mínimo es 5 s")
    @Max(value = 60, message = "tiempoLimite máximo es 60 s")
    private Integer tiempoLimite;

    @NotNull(message = "numRondas es requerido")
    @Min(value = 5,  message = "numRondas mínimo es 5")
    @Max(value = 30, message = "numRondas máximo es 30")
    private Integer numRondas;
}
