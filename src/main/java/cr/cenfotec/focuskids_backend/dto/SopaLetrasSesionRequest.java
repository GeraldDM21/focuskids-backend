package cr.cenfotec.focuskids_backend.dto;

import lombok.Data;

// Datos que manda el frontend al terminar una sesion del juego
@Data
public class SopaLetrasSesionRequest {

    private Integer perfilId;
    private String tema;
    private String nivel;
    private Integer gridSize;
    private Integer palabrasTotales;
    private Integer palabrasEncontradas;
    private Integer errores;
    private Integer tiempoUsadoSegundos;
    private Integer tiempoTotalSegundos;

    // true si el nino encontro todas las palabras antes de que se acabara el tiempo
    private Boolean completada;

    // true si la IA decidio subir el nivel por buen desempeno (mas del 30% de tiempo restante)
    private Boolean subioNivel;
}
