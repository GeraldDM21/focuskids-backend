package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.PiezasSesionRequest;
import cr.cenfotec.focuskids_backend.model.PiezasSesion;
import cr.cenfotec.focuskids_backend.service.PiezasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/piezas-tiempo")
@RequiredArgsConstructor
public class PiezasController {

    private final PiezasService service;

    @PostMapping("/sesion")
    public ResponseEntity<PiezasSesion> guardarSesion(@RequestBody PiezasSesionRequest request) {
        return ResponseEntity.ok(service.guardarSesion(request));
    }

    @GetMapping("/sesiones/{perfilId}")
    public ResponseEntity<List<PiezasSesion>> getSesiones(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(service.getSesiones(perfilId));
    }
}
