package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.juego.FinalizarSesionRequest;
import cr.cenfotec.focuskids_backend.model.SesionJuego;
import cr.cenfotec.focuskids_backend.model.SessionClickEvent;
import cr.cenfotec.focuskids_backend.service.IaRecomendacionService;
import cr.cenfotec.focuskids_backend.service.SesionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/sesiones")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
public class SesionController {

    private final SesionService            sesionService;
    private final IaRecomendacionService   iaService;

    @PostMapping("/iniciar")
    public ResponseEntity<SesionJuego> iniciar(@RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(sesionService.iniciarSesion(
                body.get("perfilId"),
                body.get("juegoId"),
                body.get("nivelId")
        ));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<SesionJuego> finalizar(@PathVariable Integer id,
                                                  @RequestBody FinalizarSesionRequest req) {
        SesionJuego sesion = sesionService.finalizarSesion(id, req);

        // Generar recomendación de nivel para la próxima sesión (best-effort)
        try {
            iaService.generarRecomendacion(
                    sesion.getPerfil().getId(),
                    sesion.getJuego().getId());
        } catch (Exception e) {
            log.warn("No se pudo generar recomendación IA para sesión {}: {}", id, e.getMessage());
        }

        return ResponseEntity.ok(sesion);
    }

    @PostMapping("/{id}/eventos")
    public ResponseEntity<SessionClickEvent> registrarEvento(@PathVariable Integer id,
                                                              @RequestBody SessionClickEvent evento) {
        return ResponseEntity.ok(sesionService.registrarEvento(id, evento));
    }

    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<List<SesionJuego>> obtenerPorPerfil(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(sesionService.obtenerPorPerfil(perfilId));
    }

    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<SessionClickEvent>> obtenerEventos(@PathVariable Integer id) {
        return ResponseEntity.ok(sesionService.obtenerEventos(id));
    }

    // ── CA-10: endpoint interno para Motor de IA ──────────────────────────
    // Acceso restringido solo a ADMINISTRADOR (servicios internos usan credenciales de admin)
    @GetMapping("/internal/metrics/session/{id}/clicks")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<SessionClickEvent>> obtenerClicksInterno(@PathVariable Integer id) {
        return ResponseEntity.ok(sesionService.obtenerEventos(id));
    }
}
