package cr.cenfotec.focuskids_backend.dto;

/**
 * Resultado de la evaluación LLM de una respuesta abierta en Historia Viva Nivel 4.
 */
public record HistoriaVivaEvaluacionResponse(
        int     puntuacion,   // 0-100: qué tan buena fue la respuesta
        String  feedback,     // Mensaje de retroalimentación en español (para el niño)
        boolean esCorrecta    // true si puntuacion >= 60
) {}
