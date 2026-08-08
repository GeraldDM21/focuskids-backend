package cr.cenfotec.focuskids_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UltimaActividadDTO {
    private String        juegoNombre;
    private String        nivel;
    private LocalDateTime fecha;
    private Integer       puntaje;
    private Boolean       completada;
}
