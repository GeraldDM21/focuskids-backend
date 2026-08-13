package cr.cenfotec.focuskids_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Nivel de dificultad que un docente o padre/tutor fija para un niño en un
 * juego específico. Mientras exista un registro para (perfil, juego), el
 * niño solo puede jugar ese nivel — el backend lo hace cumplir ignorando
 * cualquier nivel distinto que llegue desde el cliente (ver SesionService,
 * MaratonMentalService, LabCienciasService, LaberintoCognitivoService y
 * CascadaNumericaService).
 *
 * `nivel` usa las mismas tres etiquetas que NivelDificultad.nivel
 * (FACIL/MEDIO/DIFICIL); los juegos que internamente manejan un cuarto nivel
 * "EXPERTO" (Maratón Mental, Lab de Ciencias) tratan un bloqueo en DIFICIL
 * como su techo más alto bloqueable.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "nivel_asignado", uniqueConstraints = {
        @UniqueConstraint(name = "uk_nivel_asignado_perfil_juego", columnNames = {"perfil_id", "juego_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelAsignado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PerfilNino perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Juego juego;

    // FACIL | MEDIO | DIFICIL
    @Column(name = "nivel", nullable = false, length = 20)
    private String nivel;

    @Column(name = "asignado_por_usuario_id", nullable = false)
    private Integer asignadoPorUsuarioId;

    // DOCENTE | PADRE | ADMINISTRADOR
    @Column(name = "asignado_por_rol", nullable = false, length = 20)
    private String asignadoPorRol;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;
}
