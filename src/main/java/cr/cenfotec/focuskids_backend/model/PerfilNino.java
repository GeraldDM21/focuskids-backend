package cr.cenfotec.focuskids_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfil_nino")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilNino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "padre_id", nullable = false)
    private PadreTutor padre;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "avatar", length = 255)
    private String avatar;
}
