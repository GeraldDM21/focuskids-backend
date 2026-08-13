package cr.cenfotec.focuskids_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/** Datos para crear o editar una cita/recordatorio del calendario del docente. */
public record EventoCalendarioRequest(
        Integer   perfilId,     // opcional: a qué alumno se refiere (puede ser null)
        String    tipo,         // "CITA" | "RECORDATORIO"
        String    titulo,
        String    descripcion,
        LocalDate fecha,
        LocalTime hora          // opcional: null = "todo el día"
) {}
