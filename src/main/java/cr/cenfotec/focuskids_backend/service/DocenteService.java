package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.DocenteProfileUpdateRequest;
import cr.cenfotec.focuskids_backend.model.Docente;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.DocenteRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository    docenteRepository;
    private final PerfilNinoRepository perfilNinoRepository;
    private final UsuarioRepository    usuarioRepository;

    public List<Docente> listarTodos() {
        return docenteRepository.findAll();
    }

    public Docente obtenerPorUsuarioId(Integer usuarioId) {
        return docenteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado para usuario: " + usuarioId));
    }

    @Transactional
    public PerfilNino asignarDocente(Integer perfilId, Integer docenteId) {
        PerfilNino perfil = perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilId));
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado: " + docenteId));
        perfil.setDocente(docente);
        return perfilNinoRepository.save(perfil);
    }

    @Transactional
    public PerfilNino desasignarDocente(Integer perfilId) {
        PerfilNino perfil = perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilId));
        perfil.setDocente(null);
        return perfilNinoRepository.save(perfil);
    }

    /** Auto-servicio: el docente edita su propio perfil (nombre, correo, institución, grado/grupo). */
    @Transactional
    public Docente actualizarPerfil(Integer usuarioId, DocenteProfileUpdateRequest req) {
        Docente docente = docenteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado para usuario: " + usuarioId));
        Usuario usuario = docente.getUsuario();

        if (req.email() != null && !req.email().isBlank() && !req.email().equalsIgnoreCase(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(req.email())) {
                throw new RuntimeException("Ese correo ya está en uso por otra cuenta.");
            }
            usuario.setEmail(req.email());
        }
        if (req.nombre() != null && !req.nombre().isBlank()) {
            usuario.setNombre(req.nombre());
        }
        usuarioRepository.save(usuario);

        docente.setInstitucion(req.institucion());
        docente.setGradoGrupo(req.gradoGrupo());
        return docenteRepository.save(docente);
    }
}
