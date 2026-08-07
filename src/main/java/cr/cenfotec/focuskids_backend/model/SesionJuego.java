package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesion_juego")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SesionJuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilNino perfil;

    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    @ManyToOne
    @JoinColumn(name = "nivel_id", nullable = false)
    private NivelDificultad nivel;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fin")
    private LocalDateTime fin;

    @Column(name = "puntaje")
    private Integer puntaje;

    @Column(name = "completada")
    private Boolean completada;

    // ── CA-01: métricas de sesión ──────────────────────────────────────────
    @Column(name = "duracion_sesion_segundos")
    private Integer duracionSesionSegundos;

    @Column(name = "total_intentos")
    private Integer totalIntentos;

    @Column(name = "total_aciertos")
    private Integer totalAciertos;

    @Column(name = "porcentaje_aciertos", precision = 5, scale = 2)
    private BigDecimal porcentajeAciertos;

    @Column(name = "tiempo_respuesta_promedio_ms", precision = 8, scale = 2)
    private BigDecimal tiempoRespuestaPromedioMs;

    @Column(name = "racha_max_aciertos")
    private Integer rachaMaxAciertos;

    // ── CA-02: versión de configuración ───────────────────────────────────
    @Column(name = "config_version", length = 20)
    private String configVersion;

    // ── CA-05: concentración baja si todos los tiempos > 8000ms ──────────
    @Column(name = "sesion_concentracion_baja")
    private Boolean sesionConcentracionBaja;

    // ── CA-09: fallos agrupados por cuadrante de pantalla (JSON) ─────────
    @Column(name = "intentos_fallidos_por_zona", columnDefinition = "TEXT")
    private String intentosFallidosPorZona;

    // ── Motor de IA / CA-01: sesión completa y con métricas utilizables ──
    // Ver SesionService#calcularSesionValida para el criterio de validez.
    @Column(name = "sesion_valida")
    private Boolean sesionValida;
}
