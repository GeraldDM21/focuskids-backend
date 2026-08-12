package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

    @Column(name = "mensaje", columnDefinition = "TEXT")
    private String mensaje;

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