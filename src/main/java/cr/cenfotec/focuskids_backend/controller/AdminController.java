package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.LogAuditoria;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(adminService.listarUsuarios());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(adminService.obtenerUsuario(id));
    }

    @PutMapping("/usuarios/{id}/toggle-activo")
    public ResponseEntity<Usuario> toggleActivo(@PathVariable Integer id) {
        return ResponseEntity.ok(adminService.toggleActivo(id));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<LogAuditoria>> obtenerLogs() {
        return ResponseEntity.ok(adminService.obtenerLogs());
    }

    @GetMapping("/logs/usuario/{usuarioId}")
    public ResponseEntity<List<LogAuditoria>> obtenerLogsPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(adminService.obtenerLogsPorUsuario(usuarioId));
    }
}
