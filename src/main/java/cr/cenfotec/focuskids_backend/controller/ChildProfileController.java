package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.ChildProfileDTO;
import cr.cenfotec.focuskids_backend.dto.ChildProfileRequest;
import cr.cenfotec.focuskids_backend.service.ChildProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controlador que maneja las peticiones del frontend para la gestion de perfiles de ninos
// Ruta base: /api/parent/profiles
@RestController
@RequestMapping("/api/parent/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class ChildProfileController {

    private final ChildProfileService childProfileService;

    // Trae todos los perfiles que pertenecen a un padre
    @GetMapping("/padre/{padreId}")
    public ResponseEntity<List<ChildProfileDTO>> getAllProfiles(@PathVariable Integer padreId) {
        return ResponseEntity.ok(childProfileService.getAllProfilesByParent(padreId.longValue()));
    }

    // Cambia el perfil activo sin cerrar la sesion del padre
    @PostMapping("/{id}/switch/padre/{padreId}")
    public ResponseEntity<ChildProfileDTO> switchProfile(
            @PathVariable Integer id,
            @PathVariable Integer padreId) {
        return ResponseEntity.ok(childProfileService.switchProfile(id.longValue(), padreId.longValue()));
    }

    // Crea un nuevo perfil de nino para un padre
    @PostMapping("/padre/{padreId}")
    public ResponseEntity<ChildProfileDTO> createProfile(
            @Valid @RequestBody ChildProfileRequest request,
            @PathVariable Integer padreId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(childProfileService.createProfile(request, padreId.longValue()));
    }

    // Edita un perfil existente
    @PutMapping("/{id}/padre/{padreId}")
    public ResponseEntity<ChildProfileDTO> updateProfile(
            @PathVariable Integer id,
            @Valid @RequestBody ChildProfileRequest request,
            @PathVariable Integer padreId) {
        return ResponseEntity.ok(childProfileService.updateProfile(id.longValue(), request, padreId.longValue()));
    }

    // Activa o desactiva un perfil (si se desactiva, conserva el historial)
    @PatchMapping("/{id}/status/{padreId}")
    public ResponseEntity<ChildProfileDTO> toggleStatus(
            @PathVariable Integer id,
            @PathVariable Integer padreId) {
        return ResponseEntity.ok(childProfileService.toggleProfileStatus(id.longValue(), padreId.longValue()));
    }

    // Elimina un perfil. El frontend ya pidio confirmacion antes de llamar esto
    @DeleteMapping("/{id}/padre/{padreId}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Integer id,
            @PathVariable Integer padreId) {
        childProfileService.deleteProfile(id.longValue(), padreId.longValue());
        return ResponseEntity.noContent().build();
    }
}
