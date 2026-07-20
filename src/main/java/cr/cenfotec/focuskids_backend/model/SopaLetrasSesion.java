package cr.cenfotec.focuskids_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// Guarda los resultados de una sesion del juego Sopa de Letras
@Entity
@Table(name = "sopa_letras_sesion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SopaLetrasSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Id del perfil del nino que jugo
    @Column(name = "perfil_id", nullable = false)
    private Integer perfilId;

    // El tema del juego: CIENCIAS, GEOGRAFIA o MATEMATICAS
    @Column(name = "tema", length = 30, nullable = false)
    private String tema;

    // El nivel de dificultad jugado: FACIL, MEDIO, DIFICIL o EXPERTO
    @Column(name = "nivel", length = 20, nullable = false)
    private String nivel;

    // Tamano de la grilla usada en la sesion (ej: 8 significa 8x8)
    @Column(name = "grid_size", nullable = false)
    private Integer gridSize;

    @Column(name = "palabras_totales", nullable = false)
    private Integer palabrasTotales;

    @Column(name = "palabras_encontradas", nullable = false)
    private Integer palabrasEncontradas;

    // Cantidad de selecciones incorrectas que hizo el nino
    @Column(name = "errores", nullable = false)
    private Integer errores;

    @Column(name = "tiempo_usado_segundos", nullable = false)
    private Integer tiempoUsadoSegundos;

    @Column(name = "tiempo_total_segundos", nullable = false)
    private Integer tiempoTotalSegundos;

    // true si el nino encontro todas las palabras antes de que se acabara el tiempo
    @Column(name = "completada", nullable = false)
    private Boolean completada;

    // true si la IA decidio subir el nivel de dificultad por buen desempeno
    @Column(name = "subio_nivel")
    private Boolean subioNivel;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}
