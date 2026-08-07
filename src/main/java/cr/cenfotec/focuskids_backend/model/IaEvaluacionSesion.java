package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CA-03: resultado del análisis de tendencia cognitiva del Motor de IA
 * para un niño, juego y nivel específicos.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ia_evaluacion_sesion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IaEvaluacionSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nino_perfil_id", nullable = false)
    private PerfilNino ninoPerfil;

    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    @Column(name = "nivel", nullable = false, length = 50)
    private String nivel;

    @Enumerated(EnumType.STRING)
    @Column(name = "tendencia", nullable = false, length = 20)
    private TendenciaCognitiva tendencia;

    @Column(name = "confianza", nullable = false, precision = 3, scale = 2)
    private BigDecimal confianza;

    @Column(name = "sesiones_analizadas", nullable = false)
    private Integer sesionesAnalizadas;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDateTime fechaEvaluacion;
}
