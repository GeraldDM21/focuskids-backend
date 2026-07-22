package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

// RF-29: registra cada paso que da el niño dentro de un laberinto (CA-05).
@Entity
@Table(name = "laberinto_paso_evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaberintoPasoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sesion_id", nullable = false)
    @JsonIgnore
    private SesionJuego sesion;

    @Column(name = "numero_paso", nullable = false)
    private Integer numeroPaso;

    @Column(name = "direccion", nullable = false, length = 20)
    private String direccion; // ARRIBA | ABAJO | IZQUIERDA | DERECHA

    @Column(name = "posicion_x", nullable = false)
    private Integer posicionX;

    @Column(name = "posicion_y", nullable = false)
    private Integer posicionY;

    @Column(name = "es_callejon_sin_salida", nullable = false)
    private Boolean esCallejonSinSalida;

    @Column(name = "tiempo_desde_inicio_ms", nullable = false)
    private Long tiempoDesdeInicioMs;

    @Column(name = "nivel", nullable = false)
    private Integer nivel;
}
