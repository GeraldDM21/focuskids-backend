package cr.cenfotec.focuskids_backend.model;

import jakarta.persistence.*;
import lombok.*;

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
}
