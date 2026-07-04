package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "recomendacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilNino perfil;

    @ManyToOne
    @JoinColumn(name = "analisis_id", nullable = false)
    private AnalisisTendencia analisis;

    @ManyToOne
    @JoinColumn(name = "nuevo_nivel_id", nullable = false)
    private NivelDificultad nuevoNivel;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "aplicada")
    private Boolean aplicada;
}
