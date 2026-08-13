package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.NivelAsignadoRequest;
import cr.cenfotec.focuskids_backend.model.NivelAsignado;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import cr.cenfotec.focuskids_backend.service.NivelAsignadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Nivel de dificultad fijado por docente/padre para un niño en un juego
 * específico (ver NivelAsignadoService para las reglas de acceso). El niño
 * (rol NINO) nunca tiene acceso a este controlador: no puede ver ni cambiar
 * el nivel que le fue asignado — el cumplimiento real ocurre del lado del
 * backend al iniciar cada sesión de juego.
 */
@RestController
@RequestMapping("/api/nivel-asignado")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
public class NivelAsignadoController {

    private final NivelAsignadoService nivelAsignadoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<List<NivelAsignado>> listarPorPerfil(
            @PathVariable Integer perfilId,
            @AuthenticationPrincipal UserDetails me) {
        return ResponseEntity.ok(nivelAsignadoService.listarPorPerfil(perfilId, actor(me)));
    }

    @GetMapping("/perfil/{perfilId}/juego/{juegoId}")
    public ResponseEntity<NivelAsignado> obtener(
            @PathVariable Integer perfilId,
            @PathVariable Integer juegoId,
            @AuthenticationPrincipal UserDetails me) {
        return nivelAsignadoService.obtener(perfilId, juegoId, actor(me))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/perfil/{perfilId}/juego/{juegoId}")
    public ResponseEntity<NivelAsignado> asignar(
            @PathVariable Integer perfilId,
            @PathVariable Integer juegoId,
            @Valid @RequestBody NivelAsignadoRequest request,
            @AuthenticationPrincipal UserDetails me) {
        return ResponseEntity.ok(
                nivelAsignadoService.asignar(perfilId, juegoId, request.getNivel(), actor(me)));
    }

    @DeleteMapping("/perfil/{perfilId}/juego/{juegoId}")
    public ResponseEntity<Void> quitar(
            @PathVariable Integer perfilId,
            @PathVariable Integer juegoId,
            @AuthenticationPrincipal UserDetails me) {
        nivelAsignadoService.quitar(perfilId, juegoId, actor(me));
        return ResponseEntity.noContent().build();
    }

    private Usuario actor(UserDetails me) {
        return usuarioRepository.findByEmail(me.getUsername())
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado"));
    }
}
