package cr.cenfotec.focuskids_backend.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// RF-Historial CA-04: comparación de una sesión contra la sesión inmediatamente
// anterior del mismo niño en el mismo juego (si existe). Los "delta" son
// (actual - anterior): positivo en porcentajeAciertos/puntaje significa mejora,
// positivo en tiempoRespuestaPromedioMs/duracion significa que tardó más.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComparacionSesionResponse {

    private Integer sesionActualId;
    private boolean haySesionAnterior;

    private Integer sesionAnteriorId;
    private LocalDateTime fechaSesionAnterior;

    private BigDecimal porcentajeAciertosActual;
    private BigDecimal porcentajeAciertosAnterior;
    private BigDecimal deltaPorcentajeAciertos;

    private BigDecimal tiempoRespuestaPromedioMsActual;
    private BigDecimal tiempoRespuestaPromedioMsAnterior;
    private BigDecimal deltaTiempoRespuestaPromedioMs;

    private Integer puntajeActual;
    private Integer puntajeAnterior;
    private Integer deltaPuntaje;

    private Integer duracionSegundosActual;
    private Integer duracionSegundosAnterior;
    private Integer deltaDuracionSegundos;
}
