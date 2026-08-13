package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "evento_calendario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoCalendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    @JsonIgnore
    private Docente docente;

    /** Opcional: a qué alumno se refiere la cita/recordatorio (puede ser null). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PerfilNino perfil;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoEventoCalendario tipo;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    /** Opcional: si es null, el evento se muestra como "todo el día". */
    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
