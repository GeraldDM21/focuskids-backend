package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IniciarMaratonResponse {

    private Integer sesionId;
    private Integer perfilId;
    private Integer juegoId;
    private Integer nivelId;
    private String nivelSeleccionado;

    // ── Config para la fase de calibración (CA-01/02) ─────────────────────
    private Integer rondasCalibracionPorTarea;

    // ── Config para la fase dual ───────────────────────────────────────────
    private Integer rondasDuales;
    private Integer tiempoRondaMs;

    // ── Config de la Tarea A: Contar Objetos ──────────────────────────────
    private Integer objetosMin;
    private Integer objetosMax;
    private Integer opcionesConteo;

    // ── Config de la Tarea B: Identificar Color ───────────────────────────
    private Integer opcionesColor;
}
