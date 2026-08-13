package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.EventoCalendarioRequest;
import cr.cenfotec.focuskids_backend.dto.EventoCalendarioResponse;
import cr.cenfotec.focuskids_backend.model.EventoCalendario;
import cr.cenfotec.focuskids_backend.service.EventoCalendarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendario")
@RequiredArgsConstructor
public class EventoCalendarioController {

    private final EventoCalendarioService eventoCalendarioService;

    /** Calendario combinado (citas/recordatorios + asignaciones de la clase) en un rango de fechas. */
    @GetMapping("/docente/{docenteUsuarioId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<EventoCalendarioResponse>> listar(
            @PathVariable Integer docenteUsuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(eventoCalendarioService.listarCalendario(docenteUsuarioId, desde, hasta));
    }

    /** Crea una cita o recordatorio. */
    @PostMapping("/docente/{docenteUsuarioId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<EventoCalendario> crear(
            @PathVariable Integer docenteUsuarioId,
            @RequestBody EventoCalendarioRequest datos) {
        return ResponseEntity.ok(eventoCalendarioService.crear(docenteUsuarioId, datos));
    }

    /** Edita una cita o recordatorio existente (incluye mover la fecha). */
    @PutMapping("/evento/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<EventoCalendario> actualizar(
            @PathVariable Integer id,
            @RequestBody EventoCalendarioRequest datos) {
        return ResponseEntity.ok(eventoCalendarioService.actualizar(id, datos));
    }

    /** Elimina una cita o recordatorio. */
    @DeleteMapping("/evento/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        eventoCalendarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
