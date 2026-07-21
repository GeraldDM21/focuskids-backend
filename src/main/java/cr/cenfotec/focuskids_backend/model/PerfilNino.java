package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Docente docente;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    // CA-05: volumen de efectos de sonido del juego (0/25/50/75/100), persistido por perfil
    @Column(name = "volumen", nullable = false)
    private Integer volumen = 75;
}
