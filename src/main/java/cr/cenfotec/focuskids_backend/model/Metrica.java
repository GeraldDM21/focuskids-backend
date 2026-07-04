package cr.cenfotec.focuskids_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "metrica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Metrica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sesion_id", nullable = false)
    private SesionJuego sesion;

    @Column(name = "tiempo_reaccion_prom", precision = 8, scale = 2)
    private BigDecimal tiempoReaccionProm;

    @Column(name = "precision_pct", precision = 5, scale = 2)
    private BigDecimal precisionPct;

    @Column(name = "errores")
    private Integer errores;

    @Column(name = "zona_fallo", length = 100)
    private String zonaFallo;
}
