package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cascada_operacion_evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CascadaOperacionEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sesion_id", nullable = false)
    @JsonIgnore
    private SesionJuego sesion;

    @Column(name = "numero_operacion", nullable = false)
    private Integer numeroOperacion;

    @Column(name = "numero_1", nullable = false)
    private Integer numero1;

    @Column(name = "numero_2", nullable = false)
    private Integer numero2;

    @Column(name = "operador", nullable = false, length = 20)
    private String operador;

    @Column(name = "resultado_correcto", nullable = false)
    private Integer resultadoCorrecto;

    @Column(name = "respuesta_seleccionada")
    private Integer respuestaSeleccionada;

    @Column(name = "tipo_resultado", nullable = false, length = 20)
    private String tipoResultado;

    @Column(name = "tiempo_respuesta_ms")
    private Long tiempoRespuestaMs;

    @Column(name = "velocidad_caida_ms", nullable = false)
    private Integer velocidadCaidaMs;

    @Column(name = "nivel", nullable = false)
    private Integer nivel;
}