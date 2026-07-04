package cr.cenfotec.focuskids_backend.config;

import cr.cenfotec.focuskids_backend.model.Juego;
import cr.cenfotec.focuskids_backend.model.NivelDificultad;
import cr.cenfotec.focuskids_backend.repository.JuegoRepository;
import cr.cenfotec.focuskids_backend.repository.NivelDificultadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final JuegoRepository juegoRepository;
    private final NivelDificultadRepository nivelDificultadRepository;

    @Override
    public void run(String... args) {
        if (juegoRepository.count() == 0) {
            log.info("Iniciando carga de datos de los 12 juegos cognitivos...");
            seedJuegos();
            log.info("Datos iniciales cargados correctamente.");
        }
    }

    private void seedJuegos() {
        List<Object[]> juegosData = List.of(
            new Object[]{"Espejo Mental",        "ATENCION",  "Imita la secuencia de movimientos que aparece en pantalla para entrenar la memoria de trabajo y coordinación visoespacial."},
            new Object[]{"Foco Extremo",          "ATENCION",  "Presiona el botón solo cuando aparece el estímulo objetivo e ignora los distractores para entrenar la inhibición de respuesta."},
            new Object[]{"Cascada Numérica",      "CALCULO",   "Captura los números que caen y resuelve las operaciones antes de que lleguen al suelo para entrenar el cálculo mental."},
            new Object[]{"Laberinto Cognitivo",   "MEMORIA",   "Navega el laberinto recordando el camino recorrido para entrenar la memoria espacial y planificación."},
            new Object[]{"Palabras Ocultas",      "LENGUAJE",  "Encuentra las palabras escondidas en la grilla de letras para entrenar la atención selectiva y el vocabulario."},
            new Object[]{"Ritmo y Patrón",        "MEMORIA",   "Repite la secuencia de sonidos y colores en el orden correcto para entrenar la memoria auditiva y de trabajo."},
            new Object[]{"Piezas en Tiempo",      "PERCEPCION","Completa el rompecabezas antes de que se acabe el tiempo para entrenar la percepción visual y la velocidad de procesamiento."},
            new Object[]{"Reacción Controlada",   "ATENCION",  "Presiona al ver el estímulo objetivo pero espera al ver el inhibidor para entrenar el control de impulsos."},
            new Object[]{"Mapa Aventura",         "GEOGRAFIA", "Explora el mapa interactivo respondiendo preguntas geográficas para entrenar la memoria a largo plazo."},
            new Object[]{"Lab de Ciencias",       "LOGICA",    "Combina ingredientes para completar el experimento y descubrir la reacción correcta para entrenar el pensamiento lógico."},
            new Object[]{"Maratón Mental",        "CALCULO",   "Resuelve operaciones matemáticas en cadena contra el tiempo para entrenar la velocidad de cálculo y resistencia cognitiva."},
            new Object[]{"Historia Viva",         "LECTURA",   "Lee la historia y responde preguntas de comprensión inferencial con apoyo de audio para desarrollar la memoria episódica."}
        );

        for (Object[] data : juegosData) {
            Juego juego = Juego.builder()
                    .nombre((String) data[0])
                    .tipo((String) data[1])
                    .descripcion((String) data[2])
                    .activo(true)
                    .build();
            juegoRepository.save(juego);
            seedNiveles(juego);
        }
    }

    private void seedNiveles(Juego juego) {
        List<Object[]> niveles = List.of(
            new Object[]{"FACIL",  "{\"velocidad\": 1, \"distractores\": 1, \"tiempo\": 60}", new BigDecimal("0.00"), new BigDecimal("0.50")},
            new Object[]{"MEDIO",  "{\"velocidad\": 2, \"distractores\": 3, \"tiempo\": 45}", new BigDecimal("0.51"), new BigDecimal("0.75")},
            new Object[]{"DIFICIL","{\"velocidad\": 3, \"distractores\": 5, \"tiempo\": 30}", new BigDecimal("0.76"), new BigDecimal("1.00")}
        );

        for (Object[] data : niveles) {
            NivelDificultad nivel = NivelDificultad.builder()
                    .juego(juego)
                    .nivel((String) data[0])
                    .parametrosJson((String) data[1])
                    .umbralMin((BigDecimal) data[2])
                    .umbralMax((BigDecimal) data[3])
                    .build();
            nivelDificultadRepository.save(nivel);
        }
    }
}
