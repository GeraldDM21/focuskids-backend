package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "asignacion_perfil",
       uniqueConstraints = @UniqueConstraint(columnNames = {"asignacion_id", "perfil_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignacion_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "docente", "fechaCreacion"})
    private Asignacion asignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = false)
    @JsonIgnore                          // redundante: ya sabemos el perfilId
    private PerfilNino perfil;

    @Column(name = "sesiones_completadas")
    private Integer sesionesCompletadas = 0;

    @Column(name = "completada")
    private Boolean completada = false;

    @Column(name = "fecha_completada")
    private LocalDateTime fechaCompletada;
}
