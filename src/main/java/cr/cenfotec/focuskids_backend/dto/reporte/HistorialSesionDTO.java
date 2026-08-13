package cr.cenfotec.focuskids_backend.dto.reporte;

import lombok.*;
import java.math.BigDecimal;

/**
 * RF-Historial: proyección plana de SesionJuego para el historial detallado.
 * Evita referencias circulares y problemas de lazy-loading al serializar
 * la entidad JPA directamente.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialSesionDTO {

    private Integer id;
    private JuegoInfo juego;
    private NivelInfo nivel;

    private String inicio;   // ISO-8601
    private String fin;

    private Integer    puntaje;
    private Boolean    completada;
    private Integer    duracionSesionSegundos;
    private Integer    totalIntentos;
    private Integer    totalAciertos;
    private BigDecimal porcentajeAciertos;
    private BigDecimal tiempoRespuestaPromedioMs;
    private Integer    rachaMaxAciertos;
    private Boolean    sesionConcentracionBaja;
    private Boolean    sesionValida;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class JuegoInfo {
        private Integer id;
        private String  nombre;
        private String  tipo;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class NivelInfo {
        private Integer id;
        private String  nivel;
    }
}
