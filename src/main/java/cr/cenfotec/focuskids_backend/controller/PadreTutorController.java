package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.PadreTutor;
import cr.cenfotec.focuskids_backend.repository.PadreTutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/padre")
@RequiredArgsConstructor
public class PadreTutorController {

    private final PadreTutorRepository padreTutorRepository;

    /**
     * GET /api/padre/configuracion?usuarioId={id}
     * Devuelve las preferencias del padre (incluyendo resumen semanal).
     */
    @GetMapping("/configuracion")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getConfiguracion(@RequestParam Integer usuarioId) {
        return padreTutorRepository.findByUsuarioId(usuarioId)
                .map(p -> ResponseEntity.ok(Map.<String, Object>of(
                        "padreId", p.getId(),
                        "preferenciaResumenSemanal", p.getPreferenciaResumenSemanal()
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
}
