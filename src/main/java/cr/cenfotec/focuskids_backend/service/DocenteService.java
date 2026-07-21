package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.Docente;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.repository.DocenteRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository    docenteRepository;
    private final PerfilNinoRepository perfilNinoRepository;

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
}
