package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.Asignacion;
import cr.cenfotec.focuskids_backend.model.AsignacionPerfil;
import cr.cenfotec.focuskids_backend.service.AsignacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    /** Docente crea una asignación. Si perfilId viene, se asigna solo a ese alumno; si no, a toda la clase. */
    @PostMapping("/docente/{docenteUsuarioId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Asignacion> crear(
            @PathVariable Integer docenteUsuarioId,
            @RequestParam(required = false) Integer perfilId,
            @RequestBody Asignacion datos) {
        return ResponseEntity.ok(asignacionService.crear(docenteUsuarioId, datos, perfilId));
    }

    /** Lista asignaciones del docente (con progreso global). */
    @GetMapping("/docente/{docenteUsuarioId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<Asignacion>> listarPorDocente(@PathVariable Integer docenteUsuarioId) {
        return ResponseEntity.ok(asignacionService.listarPorDocente(docenteUsuarioId));
    }

    /** Lista asignaciones y progreso de un perfil de niño. */
    @GetMapping("/perfil/{perfilId}")
    @PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<AsignacionPerfil>> listarPorPerfil(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(asignacionService.listarPorPerfil(perfilId));
    }

    /** Registra una sesión completada para un niño en una asignación. */
    @PatchMapping("/{asignacionId}/progreso/{perfilId}")
    @PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<AsignacionPerfil> registrarProgreso(
            @PathVariable Integer asignacionId,
            @PathVariable Integer perfilId) {
        return ResponseEntity.ok(asignacionService.registrarProgreso(asignacionId, perfilId));
    }

    /** Docente mueve la fecha límite de una asignación (ej. desde el calendario). Body: { "fechaLimite": "2026-08-20" } */
    @PatchMapping("/{id}/fecha")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Asignacion> actualizarFecha(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        String fechaStr = body.get("fechaLimite");
        if (fechaStr == null || fechaStr.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(asignacionService.actualizarFecha(id, LocalDate.parse(fechaStr)));
    }

    /** Docente elimina una asignación. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        asignacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
