package cr.cenfotec.focuskids_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "mision_reclamada",
       uniqueConstraints = @UniqueConstraint(columnNames = {"perfil_id", "fecha"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MisionReclamada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "perfil_id", nullable = false)
    private Integer perfilId;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "mision_index", nullable = false)
    private Integer misionIndex;

    @Column(name = "recompensa", nullable = false, length = 100)
    private String recompensa;
}
