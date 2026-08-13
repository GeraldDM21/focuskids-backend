package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.DocenteProfileUpdateRequest;
import cr.cenfotec.focuskids_backend.model.CalificacionDocente;
import cr.cenfotec.focuskids_backend.model.Docente;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.repository.DocenteRepository;
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
    private final DocenteRepository          docenteRepository;

    /**
     * GET /api/docente/configuracion?usuarioId={id}
     * CA-05 (Notificaciones in-app): estado actual del interruptor del docente.
     */
    @GetMapping("/configuracion")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getConfiguracion(@RequestParam Integer usuarioId) {
        return docenteRepository.findByUsuarioId(usuarioId)
                .map(d -> ResponseEntity.ok(Map.<String, Object>of(
                        "docenteId", d.getId(),
                        "notificacionesInAppActivas", d.getNotificacionesInAppActivas()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PATCH /api/docente/notificaciones-in-app?usuarioId={id}
     * CA-05: activa o desactiva el badge de notificaciones in-app del docente.
     * Las alertas se siguen registrando en BD independientemente de este valor.
     * Body: { "activo": true | false }
     */
    @PatchMapping("/notificaciones-in-app")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> toggleNotificacionesInApp(
            @RequestParam Integer usuarioId,
            @RequestBody Map<String, Boolean> body) {

        Boolean activo = body.get("activo");
        if (activo == null) {
            return ResponseEntity.badRequest().build();
        }

        return docenteRepository.findByUsuarioId(usuarioId)
                .map(docente -> {
                    docente.setNotificacionesInAppActivas(activo);
                    docenteRepository.save(docente);
                    return ResponseEntity.ok(Map.<String, Object>of(
                            "notificacionesInAppActivas", activo
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

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

    // ── Perfil (auto-servicio) ────────────────────────────────────────────────

    /** GET /api/docente/perfil?usuarioId={id} — datos actuales del docente para el formulario de Configuración. */
    @GetMapping("/perfil")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Docente> obtenerPerfil(@RequestParam Integer usuarioId) {
        return ResponseEntity.ok(docenteService.obtenerPorUsuarioId(usuarioId));
    }

    /** PUT /api/docente/perfil?usuarioId={id} — el docente edita y guarda su propio perfil. */
    @PutMapping("/perfil")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<Docente> actualizarPerfil(
            @RequestParam Integer usuarioId,
            @RequestBody DocenteProfileUpdateRequest datos) {
        return ResponseEntity.ok(docenteService.actualizarPerfil(usuarioId, datos));
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