package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.SopaLetrasConfigDTO;
import cr.cenfotec.focuskids_backend.dto.SopaLetrasSesionRequest;
import cr.cenfotec.focuskids_backend.model.SopaLetrasSesion;
import cr.cenfotec.focuskids_backend.repository.SopaLetrasSesionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

// Logica principal del juego Sopa de Letras
@Service
@RequiredArgsConstructor
public class SopaLetrasService {

    private final SopaLetrasSesionRepository sesionRepository;

    // Palabras disponibles por tema, sin acentos para que quepan bien en la grilla
    private static final Map<String, List<String>> PALABRAS_POR_TEMA = new HashMap<>();

    static {
        PALABRAS_POR_TEMA.put("CIENCIAS", Arrays.asList(
            // Palabras cortas (4-5 letras) para nivel FACIL
            "ALGA", "ROCA", "RAIZ", "LUNA", "LARVA", "MUSGO", "VAPOR", "CALOR", "NIEVE", "ARBOL",
            // Palabras medianas (6-8 letras) para nivel MEDIO y DIFICIL
            "PLANTA", "CELULA", "TEJIDO", "REPTIL", "INSECTO", "MINERAL", "HABITAT", "ENERGIA", "OXIGENO", "ESPECIE",
            // Palabras largas (9-12 letras) para nivel EXPERTO
            "VITAMINA", "MAMIFERO", "ORGANISMO", "VERTEBRADO", "ECOSISTEMA"
        ));

        PALABRAS_POR_TEMA.put("GEOGRAFIA", Arrays.asList(
            // Cortas
            "ISLA", "LAGO", "CABO", "MAPA", "NILO", "DELTA", "COSTA", "SELVA", "BAHIA", "MONTE",
            // Medianas
            "OCEANO", "VOLCAN", "SIERRA", "GLACIAR", "DESIERTO", "LLANURA", "MESETA", "LATITUD", "EUROPA", "AFRICA",
            // Largas
            "CONTINENTE", "CORDILLERA", "PENINSULA", "LONGITUD", "ECUADOR"
        ));

        PALABRAS_POR_TEMA.put("MATEMATICAS", Arrays.asList(
            // Cortas
            "SUMA", "AREA", "CONO", "CUBO", "RESTO", "PRIMO", "PLANO", "ANGULO",
            // Medianas
            "CIRCULO", "FRACCION", "DECIMAL", "VECTOR", "MATRIZ", "NUMERO", "COCIENTE",
            // Largas
            "PERIMETRO", "TRIANGULO", "ECUACION", "PORCENTAJE", "HIPOTENUSA"
        ));
    }

    // Configuracion de cada nivel: [tamanoGrilla, cantidadPalabras, tiempoSegundos, maxLetrasPorPalabra]
    private static final Map<String, int[]> CONFIG_NIVEL = new LinkedHashMap<>();

    static {
        CONFIG_NIVEL.put("FACIL",   new int[]{8,  5, 120, 6});
        CONFIG_NIVEL.put("MEDIO",   new int[]{10, 6, 100, 8});
        CONFIG_NIVEL.put("DIFICIL", new int[]{12, 7,  90, 10});
        CONFIG_NIVEL.put("EXPERTO", new int[]{15, 8,  75, 15});
    }

    private static final List<String> ORDEN_NIVELES = Arrays.asList("FACIL", "MEDIO", "DIFICIL", "EXPERTO");

    // Devuelve la configuracion del juego segun el tema y nivel pedido
    public SopaLetrasConfigDTO getConfig(String tema, String nivel) {
        String temaUpper = tema.toUpperCase();
        String nivelUpper = nivel.toUpperCase();

        int[] config = CONFIG_NIVEL.getOrDefault(nivelUpper, CONFIG_NIVEL.get("FACIL"));
        int gridSize      = config[0];
        int numPalabras   = config[1];
        int tiempo        = config[2];
        int maxLetras     = config[3];

        // Selecciona palabras del tema que quepan en la grilla segun el nivel
        List<String> pool = PALABRAS_POR_TEMA
            .getOrDefault(temaUpper, PALABRAS_POR_TEMA.get("CIENCIAS"))
            .stream()
            .filter(p -> p.length() >= 4 && p.length() <= maxLetras && p.length() <= gridSize)
            .collect(Collectors.toList());

        // Mezcla las palabras y toma las primeras segun la cantidad del nivel
        Collections.shuffle(pool);
        List<String> seleccionadas = pool.stream().limit(numPalabras).collect(Collectors.toList());

        // Determina el siguiente nivel para el motor IA (CA-05)
        int idx = ORDEN_NIVELES.indexOf(nivelUpper);
        String nivelSiguiente = (idx >= 0 && idx < ORDEN_NIVELES.size() - 1)
            ? ORDEN_NIVELES.get(idx + 1)
            : null;

        return new SopaLetrasConfigDTO(temaUpper, nivelUpper, gridSize, seleccionadas, tiempo, nivelSiguiente);
    }

    // Guarda los resultados de una sesion terminada
    public SopaLetrasSesion guardarSesion(SopaLetrasSesionRequest request) {
        SopaLetrasSesion sesion = SopaLetrasSesion.builder()
            .perfilId(request.getPerfilId())
            .tema(request.getTema())
            .nivel(request.getNivel())
            .gridSize(request.getGridSize())
            .palabrasTotales(request.getPalabrasTotales())
            .palabrasEncontradas(request.getPalabrasEncontradas())
            .errores(request.getErrores())
            .tiempoUsadoSegundos(request.getTiempoUsadoSegundos())
            .tiempoTotalSegundos(request.getTiempoTotalSegundos())
            .completada(request.getCompletada())
            .subioNivel(request.getSubioNivel())
            .build();

        return sesionRepository.save(sesion);
    }

    // Trae el historial de sesiones de un perfil
    public List<SopaLetrasSesion> getSesionesPorPerfil(Integer perfilId) {
        return sesionRepository.findByPerfilIdOrderByCreadoEnDesc(perfilId);
    }
}
