package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.Asignacion;
import cr.cenfotec.focuskids_backend.model.AsignacionPerfil;
import cr.cenfotec.focuskids_backend.service.AsignacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    /** Docente crea una asignación para toda su clase. */
    @PostMapping("/docente/{docenteUsuarioId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Asignacion> crear(
            @PathVariable Integer docenteUsuarioId,
            @RequestBody Asignacion datos) {
        return ResponseEntity.ok(asignacionService.crear(docenteUsuarioId, datos));
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

    /** Docente elimina una asignación. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        asignacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
