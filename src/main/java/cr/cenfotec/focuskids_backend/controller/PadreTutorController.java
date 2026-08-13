package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.PadreProfileUpdateRequest;
import cr.cenfotec.focuskids_backend.model.PadreTutor;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.PadreTutorRepository;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/padre")
@RequiredArgsConstructor
public class PadreTutorController {

    private final PadreTutorRepository padreTutorRepository;
    private final UsuarioRepository    usuarioRepository;

    /**
     * GET /api/padre/configuracion?usuarioId={id}
     * Devuelve las preferencias del padre (resumen semanal y notificaciones in-app).
     */
    @GetMapping("/configuracion")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getConfiguracion(@RequestParam Integer usuarioId) {
        return padreTutorRepository.findByUsuarioId(usuarioId)
                .map(p -> ResponseEntity.ok(Map.<String, Object>of(
                        "padreId", p.getId(),
                        "preferenciaResumenSemanal", p.getPreferenciaResumenSemanal(),
                        "notificacionesInAppActivas", p.getNotificacionesInAppActivas()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PATCH /api/padre/resumen-semanal?usuarioId={id}
     * Activa o desactiva el resumen semanal para el padre.
     * Body: { "activo": true | false }
     */
    @PatchMapping("/resumen-semanal")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> toggleResumenSemanal(
            @RequestParam Integer usuarioId,
            @RequestBody Map<String, Boolean> body) {

        Boolean activo = body.get("activo");
        if (activo == null) {
            return ResponseEntity.badRequest().build();
        }

        return padreTutorRepository.findByUsuarioId(usuarioId)
                .map(padre -> {
                    padre.setPreferenciaResumenSemanal(activo);
                    padreTutorRepository.save(padre);
                    return ResponseEntity.ok(Map.<String, Object>of(
                            "preferenciaResumenSemanal", activo
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Perfil (auto-servicio) ────────────────────────────────────────────────

    /** GET /api/padre/perfil?usuarioId={id} — datos actuales del padre para el formulario de Configuración. */
    @GetMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getPerfil(@RequestParam Integer usuarioId) {
        return padreTutorRepository.findByUsuarioId(usuarioId)
                .map(p -> ResponseEntity.ok(Map.<String, Object>of(
                        "padreId", p.getId(),
                        "nombre", p.getUsuario().getNombre(),
                        "email", p.getUsuario().getEmail(),
                        "telefono", p.getTelefono() != null ? p.getTelefono() : "",
                        "relacionConNino", p.getRelacionConNino() != null ? p.getRelacionConNino() : ""
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    /** PUT /api/padre/perfil?usuarioId={id} — el padre edita y guarda su propio perfil. */
    @PutMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<Map<String, Object>> actualizarPerfil(
            @RequestParam Integer usuarioId,
            @RequestBody PadreProfileUpdateRequest datos) {

        PadreTutor padre = padreTutorRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Padre/tutor no encontrado para usuario: " + usuarioId));
        Usuario usuario = padre.getUsuario();

        if (datos.email() != null && !datos.email().isBlank() && !datos.email().equalsIgnoreCase(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(datos.email())) {
                throw new RuntimeException("Ese correo ya está en uso por otra cuenta.");
            }
            usuario.setEmail(datos.email());
        }
        if (datos.nombre() != null && !datos.nombre().isBlank()) {
            usuario.setNombre(datos.nombre());
        }
        usuarioRepository.save(usuario);

        padre.setTelefono(datos.telefono());
        padre.setRelacionConNino(datos.relacionConNino());
        PadreTutor guardado = padreTutorRepository.save(padre);

        return ResponseEntity.ok(Map.<String, Object>of(
                "padreId", guardado.getId(),
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail(),
                "telefono", guardado.getTelefono() != null ? guardado.getTelefono() : "",
                "relacionConNino", guardado.getRelacionConNino() != null ? guardado.getRelacionConNino() : ""
        ));
    }

    /**
     * PATCH /api/padre/notificaciones-in-app?usuarioId={id}
     * CA-05: activa o desactiva el badge de notificaciones in-app.
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

        return padreTutorRepository.findByUsuarioId(usuarioId)
                .map(padre -> {
                    padre.setNotificacionesInAppActivas(activo);
                    padreTutorRepository.save(padre);
                    return ResponseEntity.ok(Map.<String, Object>of(
                            "notificacionesInAppActivas", activo
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
