package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.ProgresoDashboardResponse;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import cr.cenfotec.focuskids_backend.service.ProgresoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dashboard de progreso del niño para padre o docente.
 *
 * CA-01: datos de perfil + métricas (nombre, avatar, última sesión, juego más jugado,
 *        nivel por juego, tendencia IA)
 * CA-02: ventana de 4 semanas; respuesta rápida al delegar cálculo al servicio
 * CA-03: el selector de perfil vive en el frontend; el endpoint acepta perfilId
 * CA-04: actividadSemanal calculada en ProgresoService
 * CA-05: la URL de historial completo la maneja el frontend (/padre/sesiones/{perfilId})
 */
@RestController
@RequestMapping("/api/progreso")
@RequiredArgsConstructor
public class ProgresoController {

    private final ProgresoService      progresoService;
    private final PerfilNinoRepository perfilRepo;

    /** GET /api/progreso/perfil/{perfilId} — Dashboard de un niño específico. */
    @GetMapping("/perfil/{perfilId}")
    @PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<ProgresoDashboardResponse> getProgresoPerfil(
            @PathVariable Integer perfilId) {
        return ResponseEntity.ok(progresoService.getProgreso(perfilId));
    }

    /** GET /api/progreso/padre/{padreUsuarioId} — Dashboard de todos los hijos del padre. */
    @GetMapping("/padre/{padreUsuarioId}")
    @PreAuthorize("hasAnyRole('PADRE', 'ADMINISTRADOR')")
    public ResponseEntity<List<ProgresoDashboardResponse>> getProgresoPadre(
            @PathVariable Integer padreUsuarioId) {
        List<PerfilNino> perfiles = perfilRepo.findByPadreUsuarioId(padreUsuarioId);
        return ResponseEntity.ok(progresoService.getProgresoLista(perfiles));
    }

    /** GET /api/progreso/docente/{docenteUsuarioId} — Dashboard de todos los alumnos del docente. */
    @GetMapping("/docente/{docenteUsuarioId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<ProgresoDashboardResponse>> getProgresoDocente(
            @PathVariable Integer docenteUsuarioId) {
        List<PerfilNino> perfiles = perfilRepo.findByDocenteUsuarioId(docenteUsuarioId);
        return ResponseEntity.ok(progresoService.getProgresoLista(perfiles));
    }
}
