package cr.cenfotec.focuskids_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cr.cenfotec.focuskids_backend.dto.HistoriaVivaEvaluacionRequest;
import cr.cenfotec.focuskids_backend.dto.HistoriaVivaEvaluacionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Servicio que evalúa las respuestas abiertas del Nivel 4 de Historia Viva
 * usando la API de Anthropic Claude (modelo Haiku — rápido y económico).
 *
 * Si la clave API no está configurada, devuelve una evaluación de cortesía
 * para que el juego funcione incluso en entornos sin acceso a la API.
 */
@Slf4j
@Service
public class HistoriaVivaService {

    private static final String ANTHROPIC_API_URL = "https://api.anthropic.com/v1/messages";
    private static final String MODEL             = "claude-haiku-4-5-20251001";
    private static final int    MAX_TOKENS        = 300;

    @Value("${anthropic.api.key:}")
    private String apiKey;

    private final RestClient    restClient = RestClient.create();
    private final ObjectMapper  mapper     = new ObjectMapper();

    // ─────────────────────────────────────────────────────────────────────────

    public HistoriaVivaEvaluacionResponse evaluarRespuesta(HistoriaVivaEvaluacionRequest req) {

        if (apiKey == null || apiKey.isBlank()) {
            log.warn("HistoriaVivaService: ANTHROPIC_API_KEY no configurada — devolviendo evaluación de cortesía.");
            return fallback();
        }

        try {
            String prompt = construirPrompt(req);

            // Cuerpo de la solicitud Anthropic Messages API
            Map<String, Object> body = Map.of(
                "model",      MODEL,
                "max_tokens", MAX_TOKENS,
                "messages",   List.of(Map.of("role", "user", "content", prompt))
            );

            String json = restClient.post()
                .uri(ANTHROPIC_API_URL)
                .header("x-api-key",          apiKey)
                .header("anthropic-version",   "2023-06-01")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

            return parsearRespuesta(json);

        } catch (Exception e) {
            log.error("HistoriaVivaService: error al llamar a Anthropic API — {}", e.getMessage());
            return fallback();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    private String construirPrompt(HistoriaVivaEvaluacionRequest req) {
        return """
                Eres un evaluador experto en comprensión lectora para niños de primaria.
                Tu tarea es evaluar la respuesta de un niño a una pregunta sobre un cuento
                y devolver el resultado ÚNICAMENTE en formato JSON válido, sin texto extra.

                === CUENTO: %s ===
                %s

                === PREGUNTA ===
                %s

                === GUÍA DE EVALUACIÓN (solo para ti, no la menciones) ===
                %s

                === RESPUESTA DEL NIÑO ===
                %s

                Evalúa si la respuesta demuestra comprensión real del texto.
                Devuelve SOLO este JSON (sin markdown, sin explicaciones adicionales):
                {
                  "puntuacion": <número entero 0-100>,
                  "feedback": "<mensaje de retroalimentación en español, máximo 2 oraciones, dirigido al niño, amigable y motivador>"
                }
                """.formatted(
                req.historiaTitulo(),
                req.historiaTexto(),
                req.preguntaTexto(),
                req.guiaRespuesta(),
                req.respuestaAlumno()
        );
    }

    private HistoriaVivaEvaluacionResponse parsearRespuesta(String json) throws Exception {
        // La respuesta de Anthropic tiene la forma:
        // { "content": [{ "type": "text", "text": "{...}" }], ... }
        JsonNode root     = mapper.readTree(json);
        String   textRaw  = root.path("content").get(0).path("text").asText();

        // El LLM devuelve el JSON en el campo "text"
        JsonNode result   = mapper.readTree(textRaw.trim());
        int      score    = result.path("puntuacion").asInt(50);
        String   feedback = result.path("feedback").asText("¡Buen intento! Sigue practicando.");

        score = Math.max(0, Math.min(100, score));
        return new HistoriaVivaEvaluacionResponse(score, feedback, score >= 60);
    }

    private HistoriaVivaEvaluacionResponse fallback() {
        return new HistoriaVivaEvaluacionResponse(
            60,
            "¡Gracias por tu respuesta! Benny la leyó con atención. ¡Sigue así!",
            true
        );
    }
}
