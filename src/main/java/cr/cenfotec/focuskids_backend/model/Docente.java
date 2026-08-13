package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "institucion", length = 150)
    private String institucion;

    @Column(name = "grado_grupo", length = 100)
    private String gradoGrupo;

    // CA-05 (Notificaciones in-app): mismo interruptor que el del padre,
    // aplicado al docente cuando también recibe alertas de regresión.
    @Column(name = "notificaciones_in_app_activas", nullable = false)
    @Builder.Default
    private Boolean notificacionesInAppActivas = true;
}