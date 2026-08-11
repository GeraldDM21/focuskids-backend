package cr.cenfotec.focuskids_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "piezas_sesion")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PiezasSesion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "perfil_id", nullable = false)
    private Integer perfilId;

    @Column(name = "nivel", length = 20, nullable = false)
    private String nivel;

    @Column(name = "piezas_totales")
    private Integer piezasTotales;

    @Column(name = "piezas_colocadas")
    private Integer piezasColocadas;

    @Column(name = "rotaciones")
    private Integer rotaciones;

    @Column(name = "piezas_fallidas")
    private Integer piezasFallidas;

    @Column(name = "tiempo_usado_segundos")
    private Integer tiempoUsadoSegundos;

    @Column(name = "tiempo_total_segundos")
    private Integer tiempoTotalSegundos;

    @Column(name = "puntos_bonus")
    private Integer puntosBonus;

    @Column(name = "completada")
    private Boolean completada;

    @Column(name = "subio_nivel")
    private Boolean subioNivel;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() { creadoEn = LocalDateTime.now(); }
}
