package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.LogAuditoria;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.LogAuditoriaRepository;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
    }

    @Transactional
    public Usuario toggleActivo(Integer id) {
        Usuario usuario = obtenerUsuario(id);
        usuario.setActivo(!Boolean.TRUE.equals(usuario.getActivo()));
        return usuarioRepository.save(usuario);
    }

    public List<LogAuditoria> obtenerLogs() {
        return logAuditoriaRepository.findAll();
    }

    public List<LogAuditoria> obtenerLogsPorUsuario(Integer usuarioId) {
        return logAuditoriaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }
}
