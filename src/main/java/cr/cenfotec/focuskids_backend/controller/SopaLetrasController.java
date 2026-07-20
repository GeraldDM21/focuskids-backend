package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.SopaLetrasConfigDTO;
import cr.cenfotec.focuskids_backend.dto.SopaLetrasSesionRequest;
import cr.cenfotec.focuskids_backend.model.SopaLetrasSesion;
import cr.cenfotec.focuskids_backend.service.SopaLetrasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controlador que maneja las peticiones del frontend para el juego Sopa de Letras
// Ruta base: /api/sopa-letras
@RestController
@RequestMapping("/api/sopa-letras")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SopaLetrasController {

    private final SopaLetrasService sopaLetrasService;

    // Devuelve la configuracion del juego: palabras, tamano de grilla y tiempo disponible
    // Ejemplo: GET /api/sopa-letras/config/CIENCIAS?nivel=FACIL
    @GetMapping("/config/{tema}")
    public ResponseEntity<SopaLetrasConfigDTO> getConfig(
            @PathVariable String tema,
            @RequestParam(defaultValue = "FACIL") String nivel) {
        return ResponseEntity.ok(sopaLetrasService.getConfig(tema, nivel));
    }

    // Guarda los datos de una sesion cuando el nino termina de jugar
    @PostMapping("/sesion")
    public ResponseEntity<SopaLetrasSesion> guardarSesion(
            @RequestBody SopaLetrasSesionRequest request) {
        return ResponseEntity.ok(sopaLetrasService.guardarSesion(request));
    }

    // Trae el historial de sesiones de un perfil de nino
    @GetMapping("/sesiones/{perfilId}")
    public ResponseEntity<List<SopaLetrasSesion>> getSesiones(
            @PathVariable Integer perfilId) {
        return ResponseEntity.ok(sopaLetrasService.getSesionesPorPerfil(perfilId));
    }
}
