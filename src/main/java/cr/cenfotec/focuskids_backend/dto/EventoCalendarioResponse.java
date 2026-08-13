package cr.cenfotec.focuskids_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Item combinado del calendario del docente: puede ser un evento propio
 * (cita/recordatorio, origen = "EVENTO") o una asignación de clase mostrada
 * por su fecha límite (origen = "ASIGNACION", tomada directo de la tabla de
 * asignaciones, sin duplicar datos).
 */
public record EventoCalendarioResponse(
        Integer   id,            // id del EventoCalendario o de la Asignacion, según "origen"
        String    origen,        // "EVENTO" | "ASIGNACION"
        String    tipo,          // "CITA" | "RECORDATORIO" | "ASIGNACION"
        String    titulo,
        String    descripcion,
        LocalDate fecha,
        LocalTime hora,
        Integer   perfilId,
        String    perfilNombre
) {}
