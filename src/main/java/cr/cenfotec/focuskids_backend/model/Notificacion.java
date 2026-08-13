package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "notificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "tipo", length = 50)
    private String tipo;

    /** Título corto para la tarjeta de notificación (ej. título de la asignación/cita). */
    @Column(name = "titulo", length = 200)
    private String titulo;

    @Column(name = "mensaje", columnDefinition = "TEXT")
    private String mensaje;

    /** Descripción/detalle opcional del origen (descripción de la asignación, cita o recordatorio). */
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /** Fecha del evento relacionado (fecha límite de la asignación, o fecha de la cita/recordatorio). */
    @Column(name = "fecha_evento")
    private LocalDate fechaEvento;

    /** Hora del evento relacionado, si aplica (citas). */
    @Column(name = "hora_evento")
    private LocalTime horaEvento;

    /** Correo del docente para que el padre pueda escribirle directamente. */
    @Column(name = "contacto_email", length = 150)
    private String contactoEmail;

    // CA-02: datos estructurados para el panel de la campana (nombre del
    // niño y juego afectado) — nulos para notificaciones genéricas que no
    // provienen de una alerta de regresión.
    @ManyToOne
    @JoinColumn(name = "nino_perfil_id")
    private PerfilNino ninoPerfil;

    @ManyToOne
    @JoinColumn(name = "juego_id")
    private Juego juego;

    // CA-03: ids (separados por coma) de las SesionJuego que se deben
    // resaltar al abrir el historial desde "Ver detalle".
    @Column(name = "sesiones_resaltadas", length = 100)
    private String sesionesResaltadas;

    @Column(name = "leida")
    private Boolean leida;

    @Column(name = "fecha")
    private LocalDateTime fecha;
}