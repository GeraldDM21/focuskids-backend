package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Recomendación de nivel de dificultad generada por el motor de IA.
 *
 * CA-02: tabla ia_recomendacion con campos: nino_perfil_id, juego_id,
 *        nivel_recomendado, nivel_anterior, motivo, fecha_recomendacion.
 * CA-05: sesiones_origen almacena el JSON array de IDs de sesiones usadas.
 */
@Entity
@Table(name = "ia_recomendacion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IaRecomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ── Contexto ──────────────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nino_perfil_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PerfilNino perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Juego juego;

    // ── Niveles ───────────────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nivel_recomendado_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private NivelDificultad nivelRecomendado;

    /** Nivel que tenía el niño antes de la recomendación (para auditoría). */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nivel_anterior_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private NivelDificultad nivelAnterior;

    // ── Algoritmo ─────────────────────────────────────────────────────────
    /** Tendencia detectada: MEJORA | ESTANCAMIENTO | REGRESION */
    @Column(name = "tendencia", nullable = false, length = 20)
    private String tendencia;

    /** Proporción de sesiones que coinciden con la tendencia (0.0 – 1.0). */
    @Column(name = "confianza", precision = 5, scale = 4)
    private BigDecimal confianza;

    // ── Auditoría ─────────────────────────────────────────────────────────
    /** Explicación en lenguaje natural de la decisión. */
    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "fecha_recomendacion", nullable = false)
    private LocalDateTime fechaRecomendacion;

    /** CA-05: JSON array con los IDs de las sesiones que originaron esta recomendación. */
    @Column(name = "sesiones_origen", columnDefinition = "TEXT")
    private String sesionesOrigen;
}
