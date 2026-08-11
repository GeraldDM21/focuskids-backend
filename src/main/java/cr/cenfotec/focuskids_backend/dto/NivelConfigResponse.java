package cr.cenfotec.focuskids_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta que incluye los metadatos del nivel y sus parámetros actuales.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelConfigResponse {

    private Integer nivelId;
    private Integer juegoId;
    private String  juegoNombre;
    private String  nivel;           // FACIL | MEDIO | DIFICIL
    private String  configVersion;   // e.g. "v3_1706789012345"

    // Parámetros actuales
    private Integer velocidadEstimulos;
    private Integer cantidadElementos;
    private Integer tiempoLimite;
    private Integer numRondas;
}
