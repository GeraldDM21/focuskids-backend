package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "analisis_tendencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalisisTendencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilNino perfil;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "score_tendencia", precision = 5, scale = 2)
    private BigDecimal scoreTendencia;

    @Column(name = "ventana_sesiones")
    private Integer ventanaSesiones;
}
