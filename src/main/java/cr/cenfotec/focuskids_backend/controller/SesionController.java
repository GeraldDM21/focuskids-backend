package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.SesionJuego;
import cr.cenfotec.focuskids_backend.model.SessionClickEvent;
import cr.cenfotec.focuskids_backend.service.SesionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sesiones")
@RequiredArgsConstructor
public class SesionController {

    private final SesionService sesionService;

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
                                                  @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(sesionService.finalizarSesion(id, body.get("puntaje")));
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
}
