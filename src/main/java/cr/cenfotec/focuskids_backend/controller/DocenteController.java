package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.CalificacionDocente;
import cr.cenfotec.focuskids_backend.model.Docente;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.service.CalificacionDocenteService;
import cr.cenfotec.focuskids_backend.service.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/docente")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService            docenteService;
    private final CalificacionDocenteService calificacionService;

    /** Lista todos los docentes registrados (para que el padre pueda elegir uno). */
    @GetMapping("/lista")
    @PreAuthorize("hasAnyRole('PADRE', 'ADMINISTRADOR')")
    public ResponseEntity<List<Docente>> listar() {
        return ResponseEntity.ok(docenteService.listarTodos());
    }

    /** Asigna un docente al perfil de un niño. */
    @PatchMapping("/asignar/{perfilId}/{docenteId}")
    @PreAuthorize("hasAnyRole('PADRE', 'ADMINISTRADOR')")
    public ResponseEntity<PerfilNino> asignar(
            @PathVariable Integer perfilId,
            @PathVariable Integer docenteId) {
        return ResponseEntity.ok(docenteService.asignarDocente(perfilId, docenteId));
    }

    /** Elimina la asignación de docente de un perfil. */
    @DeleteMapping("/desasignar/{perfilId}")
    @PreAuthorize("hasAnyRole('PADRE', 'ADMINISTRADOR')")
    public ResponseEntity<PerfilNino> desasignar(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(docenteService.desasignarDocente(perfilId));
    }

    // ── Calificaciones ──────────────────────────────────────────────────────

    @GetMapping("/{docenteId}/calificaciones")
    @PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<CalificacionDocente>> calificaciones(@PathVariable Integer docenteId) {
        return ResponseEntity.ok(calificacionService.listarPorDocente(docenteId));
    }

    @GetMapping("/{docenteId}/calificaciones/resumen")
    @PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> resumen(@PathVariable Integer docenteId) {
        return ResponseEntity.ok(calificacionService.resumenDocente(docenteId));
    }

    @PostMapping("/{docenteId}/calificaciones")
    @PreAuthorize("hasRole('PADRE')")
    public ResponseEntity<CalificacionDocente> calificar(
            @PathVariable Integer docenteId,
            @RequestBody Map<String, Object> body) {
        Integer padreUsuarioId = (Integer) body.get("padreUsuarioId");
        Integer puntuacion     = (Integer) body.get("puntuacion");
        String  comentario     = (String)  body.get("comentario");
        return ResponseEntity.ok(calificacionService.calificar(padreUsuarioId, docenteId, puntuacion, comentario));
    }
}
