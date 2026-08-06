package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

// Representa UNA ronda del juego Maraton Mental. Una ronda puede ser de
// calibracion individual (solo Tarea A o solo Tarea B activa) o de fase dual
// (ambas tareas activas al mismo tiempo, pantalla dividida). Guardamos el
// resultado de las dos tareas en la misma fila porque en fase dual ocurren
// exactamente en el mismo instante para el nino.
@Entity
@Table(name = "maraton_mental_ronda")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaratonMentalRondaEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    private SesionJuego sesion;

    @Column(name = "numero_ronda", nullable = false)
    private Integer numeroRonda;

    // CALIBRACION_A | CALIBRACION_B | DUAL
    @Column(name = "fase", nullable = false, length = 20)
    private String fase;

    // ── Tarea A: Contar Objetos ──────────────────────────────────────────
    @Column(name = "tarea_a_respondida", nullable = false)
    private Boolean tareaARespondida;

    @Column(name = "tarea_a_correcta")
    private Boolean tareaACorrecta;

    @Column(name = "tarea_a_tiempo_ms")
    private Long tareaATiempoRespuestaMs;

    // ── Tarea B: Identificar Color ───────────────────────────────────────
    @Column(name = "tarea_b_respondida", nullable = false)
    private Boolean tareaBRespondida;

    @Column(name = "tarea_b_correcta")
    private Boolean tareaBCorrecta;

    @Column(name = "tarea_b_tiempo_ms")
    private Long tareaBTiempoRespuestaMs;

    @Column(name = "nivel", nullable = false, length = 20)
    private String nivel;
}
