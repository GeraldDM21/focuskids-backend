package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "session_click_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sesion_id", nullable = false)
    private SesionJuego sesion;

    @Column(name = "click_x")
    private Integer clickX;

    @Column(name = "click_y")
    private Integer clickY;

    @Column(name = "elemento_id", length = 100)
    private String elementoId;

    @Column(name = "timestamp_ms")
    private Long timestampMs;

    @Column(name = "fue_acierto")
    private Boolean fueAcierto;
}
