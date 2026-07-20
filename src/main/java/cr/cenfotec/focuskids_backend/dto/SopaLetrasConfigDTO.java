package cr.cenfotec.focuskids_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// Respuesta del backend con todo lo que necesita el frontend para iniciar el juego
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SopaLetrasConfigDTO {

    // El tema que se jugo: CIENCIAS, GEOGRAFIA o MATEMATICAS
    private String tema;

    // El nivel de dificultad: FACIL, MEDIO, DIFICIL o EXPERTO
    private String nivel;

    // Tamano de la grilla (ej: 8 significa una grilla de 8x8)
    private int gridSize;

    // Lista de palabras que el nino tiene que encontrar en la grilla
    private List<String> palabras;

    // Tiempo disponible para completar el juego en segundos
    private int tiempoSegundos;

    // El nombre del siguiente nivel de dificultad, o null si ya esta en el maximo
    private String nivelSiguiente;
}
