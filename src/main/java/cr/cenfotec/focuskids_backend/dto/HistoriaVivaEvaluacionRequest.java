package cr.cenfotec.focuskids_backend.dto;

/**
 * Solicitud de evaluación de respuesta abierta para Historia Viva Nivel 4.
 * El frontend envía el contexto necesario para que el LLM puntúe la respuesta.
 */
public record HistoriaVivaEvaluacionRequest(
        String historiaTitulo,   // Título de la historia
        String historiaTexto,    // Texto completo del cuento
        String preguntaTexto,    // La pregunta que se hizo al niño
        String guiaRespuesta,    // Guía interna (no visible al niño) para orientar al LLM
        String respuestaAlumno   // Respuesta escrita por el niño
) {}
