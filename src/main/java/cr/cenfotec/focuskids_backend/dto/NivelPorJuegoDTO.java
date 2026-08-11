package cr.cenfotec.focuskids_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NivelPorJuegoDTO {
    private String  juegoNombre;
    private String  nivel;        // "FACIL", "MEDIO", "DIFICIL"
    private Integer sesiones;     // cantidad de sesiones jugadas en 4 semanas
    private Integer pctSesiones;  // % relativo al juego más jugado (para la barra)
}
