package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.NivelConfigRequest;
import cr.cenfotec.focuskids_backend.dto.NivelConfigResponse;
import cr.cenfotec.focuskids_backend.model.AdminConfigAudit;
import cr.cenfotec.focuskids_backend.service.AdminConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints para la configuración de parámetros de dificultad por nivel.
 *
 * CA-01: @PreAuthorize("hasRole('ADMINISTRADOR')") aplica a TODOS los métodos
 *        de esta clase. El SecurityConfig ya protege /api/admin/** con hasRole,
 *        pero esta anotación garantiza la verificación independiente en backend.
 *        HTTP 403 si el rol no coincide; HTTP 401 si el token expiró o es nulo.
 */
@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminConfigController {

    private final AdminConfigService configService;

    /**
     * Devuelve la configuración de todos los juegos y niveles.
     * El frontend los agrupa por juego para mostrar la tabla de edición.
     */
    @GetMapping("/niveles")
    public ResponseEntity<List<NivelConfigResponse>> getTodasLasConfiguraciones() {
        return ResponseEntity.ok(configService.getTodasLasConfiguraciones());
    }

    /**
     * Devuelve los 3 niveles de un juego específico.
     */
    @GetMapping("/juego/{juegoId}/niveles")
    public ResponseEntity<List<NivelConfigResponse>> getConfiguracionesPorJuego(
            @PathVariable Integer juegoId) {
        return ResponseEntity.ok(configService.getConfiguracionesPorJuego(juegoId));
    }

    /**
     * Actualiza los parámetros de un nivel.
     *
     * CA-02: la validación @Valid devuelve HTTP 400 con detalle si algún valor
     *        está fuera de rango (manejado por GlobalExceptionHandler).
     * CA-03: se genera una nueva config_version; sesiones activas conservan la anterior.
     * CA-05: cada llamada exitosa registra un log inmutable en admin_config_audit.
     */
    @PutMapping("/nivel/{nivelId}")
    public ResponseEntity<NivelConfigResponse> actualizarConfiguracion(
            @PathVariable Integer nivelId,
            @Valid @RequestBody NivelConfigRequest request,
            HttpServletRequest httpRequest) {

        String ip = obtenerIp(httpRequest);
        return ResponseEntity.ok(configService.actualizarConfiguracion(nivelId, request, ip));
    }

    /**
     * Últimas 50 entradas del log de auditoría (CA-05).
     */
    @GetMapping("/audit")
    public ResponseEntity<List<AdminConfigAudit>> getAudit() {
        return ResponseEntity.ok(configService.getAuditRecientes());
    }

    /**
     * Extrae la IP real del cliente (considera proxies con X-Forwarded-For).
     */
    private String obtenerIp(HttpServletRequest req) {
        String forwarded = req.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }
}
