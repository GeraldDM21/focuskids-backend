package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "padre_tutor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PadreTutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "relacion_con_nino", length = 50)
    private String relacionConNino;

    @Column(name = "preferencia_resumen_semanal", nullable = false)
    @Builder.Default
    private Boolean preferenciaResumenSemanal = true;

    // CA-05 (Notificaciones in-app): si es false, el padre sigue recibiendo
    // las alertas en BD pero el badge de la campana no se muestra en el front.
    @Column(name = "notificaciones_in_app_activas", nullable = false)
    @Builder.Default
    private Boolean notificacionesInAppActivas = true;
}