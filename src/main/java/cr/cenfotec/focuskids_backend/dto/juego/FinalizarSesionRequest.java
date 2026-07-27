package cr.cenfotec.focuskids_backend.dto.juego;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Payload que el frontend envía al finalizar una sesión de juego.
 * Contiene todas las métricas calculadas en el cliente (CA-01 a CA-09).
 */
@Data
public class FinalizarSesionRequest {

    // ── Básico ────────────────────────────────────────────────────────────
    private Integer puntaje;

    // ── CA-01: métricas de sesión ─────────────────────────────────────────
    private Integer totalIntentos;
    private Integer totalAciertos;
    private BigDecimal porcentajeAciertos;
    private BigDecimal tiempoRespuestaPromedioMs;   // null si CA-05 aplica
    private Integer rachaMaxAciertos;

    // ── CA-02: versión de config leída al inicio ──────────────────────────
    private String configVersion;

    // ── CA-05: concentración baja ─────────────────────────────────────────
    private Boolean sesionConcentracionBaja;

    // ── CA-09: fallos por cuadrante como JSON string ──────────────────────
    // Ej: {"superiorIzquierdo":2,"superiorDerecho":0,"inferiorIzquierdo":1,"inferiorDerecho":3}
    private String intentosFallidosPorZona;
}
