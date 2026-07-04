package cr.cenfotec.focuskids_backend.model;

import jakarta.persistence.*;
import lombok.*;

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
}
