package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ia_alerta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IaAlerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilNino perfil;

    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    @Column(name = "nivel", length = 50)
    private String nivel;

    @ManyToOne
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoAlerta estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal", nullable = false, length = 10)
    private CanalAlerta canal;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;
}