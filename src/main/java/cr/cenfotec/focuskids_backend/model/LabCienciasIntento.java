package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lab_ciencias_intento")
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabCienciasIntento {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "sesion_id",
            nullable = false
    )
    private SesionJuego sesion;

    @Column(
            name = "numero_experimento",
            nullable = false
    )
    private Integer numeroExperimento;

    @Column(
            name = "experimento_codigo",
            nullable = false,
            length = 60
    )
    private String experimentoCodigo;

    @Column(
            name = "ingrediente_1",
            nullable = false,
            length = 60
    )
    private String ingrediente1;

    @Column(
            name = "ingrediente_2",
            nullable = false,
            length = 60
    )
    private String ingrediente2;

    @Column(
            name = "ingredientes_seleccionados",
            length = 300
    )
    private String ingredientesSeleccionados;

    @Column(
            name = "exitoso",
            nullable = false
    )
    private Boolean exitoso;

    @Column(
            name = "tiempo_intento_ms",
            nullable = false
    )
    private Long tiempoIntentoMs;

    @Column(
            name = "intentos_acumulados",
            nullable = false
    )
    private Integer intentosAcumuladosExperimento;

    @Column(
            name = "nivel",
            nullable = false,
            length = 20
    )
    private String nivel;
}