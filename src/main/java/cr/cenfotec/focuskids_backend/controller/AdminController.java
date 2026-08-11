package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.UsuarioEditRequest;
import cr.cenfotec.focuskids_backend.model.LogAuditoria;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.service.AdminService;
import cr.cenfotec.focuskids_backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminController {

    private final AdminService adminService;

    // ── Usuarios ──────────────────────────────────────────────────────────────

    /** CA-01: lista simple (compatibilidad). */
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(adminService.listarUsuarios());
    }

    /** CA-01: búsqueda paginada con filtros. */
    @GetMapping("/usuarios/buscar")
    public ResponseEntity<Page<Usuario>> buscarUsuarios(
            @RequestParam(defaultValue = "")  String q,
            @RequestParam(defaultValue = "")  String rol,
            @RequestParam(defaultValue = "0") int    page,
            @AuthenticationPrincipal UserDetails me) {
        return ResponseEntity.ok(adminService.buscarUsuarios(q, rol, page));
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(adminService.obtenerUsuario(id));
    }

    /** CA-03: activa o desactiva un usuario. */
    @PutMapping("/usuarios/{id}/toggle-activo")
    public ResponseEntity<Usuario> toggleActivo(
            @PathVariable Integer id,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails me) {
        return ResponseEntity.ok(adminService.toggleActivo(id, obtenerIp(request)));
    }

    /**
     * CA-02: editar nombre, rol y/o estado de un usuario.
     * CA-05: log de auditoría registrado en el servicio.
     */
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> editarUsuario(
            @PathVariable Integer id,
            @RequestBody UsuarioEditRequest request,
            HttpServletRequest httpRequest,
            @AuthenticationPrincipal UserDetails me) {
        Integer adminId = adminService.getIdPorEmail(me.getUsername());
        return ResponseEntity.ok(
                adminService.editarUsuario(id, request, adminId, obtenerIp(httpRequest)));
    }

    /**
     * CA-04: eliminar usuario con doble validación de perfiles activos.
     * CA-05: log de auditoría registrado en el servicio.
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Integer id,
            HttpServletRequest httpRequest,
            @AuthenticationPrincipal UserDetails me) {
        Integer adminId = adminService.getIdPorEmail(me.getUsername());
        adminService.eliminarUsuario(id, adminId, obtenerIp(httpRequest));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ninos")
    public ResponseEntity<List<PerfilNino>> listarNinos() {
        return ResponseEntity.ok(adminService.listarNinos());
    }

    // ── Logs (legacy) ─────────────────────────────────────────────────────────

    /** @deprecated Usar /logs/filtrados. Mantenido para compatibilidad. */
    @GetMapping("/logs")
    public ResponseEntity<List<LogAuditoria>> obtenerLogs() {
        return ResponseEntity.ok(adminService.obtenerLogs());
    }

    @GetMapping("/logs/usuario/{usuarioId}")
    public ResponseEntity<List<LogAuditoria>> obtenerLogsPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(adminService.obtenerLogsPorUsuario(usuarioId));
    }

    // ── Logs paginados con filtros (CA-01 … CA-05) ────────────────────────────

    /**
     * GET /api/admin/logs/filtrados
     * Parámetros opcionales: page (default 0), fechaDesde, fechaHasta, accion, usuarioId
     * CA-04: 25 por página. CA-05: sólo últimos 90 días.
     */
    @GetMapping("/logs/filtrados")
    public ResponseEntity<Page<LogAuditoria>> obtenerLogsFiltrados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) Integer usuarioId) {

        Page<LogAuditoria> resultado = adminService.obtenerLogsFiltrados(
                page, fechaDesde, fechaHasta,
                (accion != null && accion.isBlank()) ? null : accion,
                usuarioId);
        return ResponseEntity.ok(resultado);
    }

    /**
     * GET /api/admin/logs/exportar-csv
     * Devuelve el CSV del filtro actual como descarga.
     * CA-02: "Exportar filtro actual a CSV".
     * CA-03: sólo lectura — no hay endpoints DELETE ni PUT en logs.
     */
    @GetMapping("/logs/exportar-csv")
    public ResponseEntity<byte[]> exportarLogsCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) Integer usuarioId) {

        byte[] csv = adminService.exportarLogsCsv(
                fechaDesde, fechaHasta,
                (accion != null && accion.isBlank()) ? null : accion,
                usuarioId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"logs-auditoria.csv\"")
                .body(csv);
    }

    // ── Utilidades ────────────────────────────────────────────────────────────

    private String obtenerIp(HttpServletRequest req) {
        String forwarded = req.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }
}
