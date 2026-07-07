package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.Juego;
import cr.cenfotec.focuskids_backend.model.NivelDificultad;
import cr.cenfotec.focuskids_backend.service.JuegoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos")
@RequiredArgsConstructor
public class JuegoController {

    private final JuegoService juegoService;

    @GetMapping
    public ResponseEntity<List<Juego>> listarActivos() {
        return ResponseEntity.ok(juegoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Juego> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(juegoService.obtenerPorId(id));
    }

    @GetMapping("/{id}/niveles")
    public ResponseEntity<List<NivelDificultad>> obtenerNiveles(@PathVariable Integer id) {
        return ResponseEntity.ok(juegoService.obtenerNiveles(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Juego> crear(@RequestBody Juego juego) {
        return ResponseEntity.ok(juegoService.guardar(juego));
    }
}
