package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.Juego;
import cr.cenfotec.focuskids_backend.model.NivelAsignado;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.JuegoRepository;
import cr.cenfotec.focuskids_backend.repository.NivelAsignadoRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Nivel de dificultad "bloqueado" por juego que el docente o el padre/tutor
 * de un niño puede fijar: mientras exista, el niño solo puede jugar ese
 * nivel (el niño no puede verlo ni modificarlo — CA análogo al de la
 * recomendación de IA). ADMINISTRADOR puede gestionar cualquier perfil;
 * DOCENTE y PADRE solo los perfiles que tienen asignados.
 */
@Service
@RequiredArgsConstructor
public class NivelAsignadoService {

    private static final Set<String> NIVELES_VALIDOS = Set.of("FACIL", "MEDIO", "DIFICIL");

    private final NivelAsignadoRepository nivelAsignadoRepository;
    private final PerfilNinoRepository perfilNinoRepository;
    private final JuegoRepository juegoRepository;

    // @Transactional (aunque son solo lecturas): PerfilNino.docente es LAZY, y
    // verificarAcceso necesita leer perfil.getDocente().getUsuario() dentro de
    // la misma sesión de Hibernate que cargó el perfil.
    @Transactional(readOnly = true)
    public List<NivelAsignado> listarPorPerfil(Integer perfilId, Usuario actor) {
        PerfilNino perfil = obtenerPerfil(perfilId);
        verificarAcceso(perfil, actor);
        return nivelAsignadoRepository.findByPerfilId(perfilId);
    }

    @Transactional(readOnly = true)
    public Optional<NivelAsignado> obtener(Integer perfilId, Integer juegoId, Usuario actor) {
        PerfilNino perfil = obtenerPerfil(perfilId);
        verificarAcceso(perfil, actor);
        return nivelAsignadoRepository.findByPerfilIdAndJuegoId(perfilId, juegoId);
    }

    @Transactional
    public NivelAsignado asignar(Integer perfilId, Integer juegoId, String nivel, Usuario actor) {
        String nivelNormalizado = nivel == null ? "" : nivel.trim().toUpperCase();
        if (!NIVELES_VALIDOS.contains(nivelNormalizado)) {
            throw new IllegalArgumentException("nivel debe ser FACIL, MEDIO o DIFICIL");
        }

        PerfilNino perfil = obtenerPerfil(perfilId);
        verificarAcceso(perfil, actor);

        Juego juego = juegoRepository.findById(juegoId)
                .orElseThrow(() -> new IllegalArgumentException("Juego no encontrado: " + juegoId));

        NivelAsignado asignado = nivelAsignadoRepository.findByPerfilIdAndJuegoId(perfilId, juegoId)
                .orElseGet(() -> NivelAsignado.builder()
                        .perfil(perfil)
                        .juego(juego)
                        .build());

        asignado.setNivel(nivelNormalizado);
        asignado.setAsignadoPorUsuarioId(actor.getId());
        asignado.setAsignadoPorRol(actor.getRol().name());
        asignado.setFechaAsignacion(LocalDateTime.now());

        return nivelAsignadoRepository.save(asignado);
    }

    @Transactional
    public void quitar(Integer perfilId, Integer juegoId, Usuario actor) {
        PerfilNino perfil = obtenerPerfil(perfilId);
        verificarAcceso(perfil, actor);
        nivelAsignadoRepository.deleteByPerfilIdAndJuegoId(perfilId, juegoId);
    }

    private PerfilNino obtenerPerfil(Integer perfilId) {
        return perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado: " + perfilId));
    }

    /**
     * ADMINISTRADOR: acceso total. DOCENTE: solo si es el docente asignado
     * al perfil. PADRE: solo si es el padre/tutor del perfil.
     */
    private void verificarAcceso(PerfilNino perfil, Usuario actor) {
        switch (actor.getRol().name()) {
            case "ADMINISTRADOR" -> {
                // acceso total
            }
            case "DOCENTE" -> {
                if (perfil.getDocente() == null
                        || perfil.getDocente().getUsuario() == null
                        || !perfil.getDocente().getUsuario().getId().equals(actor.getId())) {
                    throw new AccessDeniedException("No tiene permisos sobre este perfil");
                }
            }
            case "PADRE" -> {
                if (perfil.getPadre() == null
                        || perfil.getPadre().getUsuario() == null
                        || !perfil.getPadre().getUsuario().getId().equals(actor.getId())) {
                    throw new AccessDeniedException("No tiene permisos sobre este perfil");
                }
            }
            default -> throw new AccessDeniedException("Rol sin permisos para gestionar niveles asignados");
        }
    }
}
