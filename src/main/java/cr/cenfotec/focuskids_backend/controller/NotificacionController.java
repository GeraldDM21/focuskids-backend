package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.Notificacion;
import cr.cenfotec.focuskids_backend.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notificacion>> obtenerNoLeidas(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(notificacionService.obtenerNoLeidas(usuarioId));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarLeida(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.marcarLeida(id));
    }

    @PutMapping("/usuario/{usuarioId}/leer-todas")
    public ResponseEntity<Void> marcarTodasLeidas(@PathVariable Integer usuarioId) {
        notificacionService.marcarTodasLeidas(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Notificacion> crear(@PathVariable Integer usuarioId,
                                               @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(notificacionService.crear(
                usuarioId,
                body.get("tipo"),
                body.get("mensaje")
        ));
    }
}
