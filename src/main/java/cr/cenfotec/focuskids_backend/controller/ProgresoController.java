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

@RestController
@RequestMapping("/api/progreso")
@RequiredArgsConstructor
public class ProgresoController {

    private final ProgresoService      progresoService;
    private final PerfilNinoRepository perfilRepo;

    /** Progreso de un perfil específico */
    @GetMapping("/perfil/{perfilId}")
    @PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<ProgresoDashboardResponse> getProgresoPerfil(
            @PathVariable Integer perfilId) {
        return ResponseEntity.ok(progresoService.getProgreso(perfilId));
    }

    /** Todos los perfiles de un padre */
    @GetMapping("/padre/{padreUsuarioId}")
    @PreAuthorize("hasAnyRole('PADRE', 'ADMINISTRADOR')")
    public ResponseEntity<List<ProgresoDashboardResponse>> getProgresoPadre(
            @PathVariable Integer padreUsuarioId) {
        List<PerfilNino> perfiles = perfilRepo.findByPadreUsuarioId(padreUsuarioId);
        return ResponseEntity.ok(progresoService.getProgresoLista(perfiles));
    }

    /** Todos los perfiles asignados a un docente */
    @GetMapping("/docente/{docenteUsuarioId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<ProgresoDashboardResponse>> getProgresoDocente(
            @PathVariable Integer docenteUsuarioId) {
        List<PerfilNino> perfiles = perfilRepo.findByDocenteUsuarioId(docenteUsuarioId);
        return ResponseEntity.ok(progresoService.getProgresoLista(perfiles));
    }
}
