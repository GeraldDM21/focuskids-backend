package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "asignacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    @JsonIgnore                          // no se expone al frontend
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Juego juego;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "minimo_sesiones")
    private Integer minimoSesiones = 1;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    /** Tema de vocabulario para Palabras Ocultas (CIENCIAS, GEOGRAFIA, MATEMATICAS). Null para otros juegos. */
    @Column(name = "tema", length = 50)
    private String tema;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    /** Campo de solo-request (no se persiste): si el docente indica un
     *  alumno puntual, la asignación se enlaza únicamente a ese perfil en
     *  vez de a toda la clase. Ver AsignacionService.crear(). */
    @Transient
    private Integer perfilId;

    /** Campo de solo-respuesta (no se persiste): nombres de los alumnos
     *  enlazados a esta asignación. Se rellena al listar. */
    @Transient
    private List<String> alumnosAsignados;

    /** Calculado en el servicio (no persistido): a cuántos alumnos se asignó realmente. */
    @Transient
    private Integer cantidadAlumnos;

    /** Calculado en el servicio (no persistido): si se asignó a un solo alumno, su nombre. */
    @Transient
    private String alumnoNombre;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
