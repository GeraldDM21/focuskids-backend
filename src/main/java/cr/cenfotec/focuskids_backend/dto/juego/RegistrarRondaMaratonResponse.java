package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrarRondaMaratonResponse {

    private Integer rondaId;
    private Integer rondasRegistradas;

    // CA-05: aviso de que el motor detectó costo dual > 40% para esa tarea.
    // Una vez el frontend recibe "true", debe mantener la tarea reducida
    // por el resto de la sesión (no revertir aunque luego vuelva a bajar del umbral).
    private Boolean reducirTareaA;
    private Boolean reducirTareaB;

    // Valores informativos (pueden ser null si aún no hay suficiente muestra)
    private Double costoDualAPorcentaje;
    private Double costoDualBPorcentaje;
}
