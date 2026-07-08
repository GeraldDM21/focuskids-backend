package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "nivel_dificultad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelDificultad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    @Column(name = "nivel", nullable = false, length = 50)
    private String nivel;

    @Column(name = "parametros_json", columnDefinition = "JSON")
    private String parametrosJson;

    @Column(name = "umbral_min", precision = 5, scale = 2)
    private BigDecimal umbralMin;

    @Column(name = "umbral_max", precision = 5, scale = 2)
    private BigDecimal umbralMax;
}
