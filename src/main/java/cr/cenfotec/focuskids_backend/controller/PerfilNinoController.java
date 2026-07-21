package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.service.PerfilNinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
public class PerfilNinoController {

    private final PerfilNinoService perfilNinoService;

    @GetMapping("/padre/{padreId}")
    public ResponseEntity<List<PerfilNino>> obtenerPorPadre(@PathVariable Integer padreId) {
        return ResponseEntity.ok(perfilNinoService.obtenerPorPadre(padreId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilNino> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(perfilNinoService.obtenerPorId(id));
    }

    @PostMapping("/padre/{padreId}")
    public ResponseEntity<PerfilNino> crear(@PathVariable Integer padreId,
                                             @RequestBody PerfilNino perfil) {
        return ResponseEntity.ok(perfilNinoService.crear(padreId, perfil));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilNino> actualizar(@PathVariable Integer id,
                                                  @RequestBody PerfilNino perfil) {
        return ResponseEntity.ok(perfilNinoService.actualizar(id, perfil));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<PerfilNino> toggleActivo(@PathVariable Integer id) {
        return ResponseEntity.ok(perfilNinoService.toggleActivo(id));
    }

    // CA-05: persistir nivel de volumen de efectos de sonido (0/25/50/75/100) por perfil
    @PatchMapping("/{id}/volumen")
    public ResponseEntity<PerfilNino> actualizarVolumen(@PathVariable Integer id,
                                                         @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(perfilNinoService.actualizarVolumen(id, body.get("volumen")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        perfilNinoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
