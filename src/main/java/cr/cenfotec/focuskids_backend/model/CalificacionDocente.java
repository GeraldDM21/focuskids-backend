package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "calificacion_docente",
       uniqueConstraints = @UniqueConstraint(columnNames = {"padre_id", "docente_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PadreTutor padre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Docente docente;

    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion; // 1-5

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @PrePersist
    protected void onCreate() { this.fecha = LocalDateTime.now(); }
}
