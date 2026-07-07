package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.PadreTutor;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.repository.PadreTutorRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfilNinoService {

    private final PerfilNinoRepository perfilNinoRepository;
    private final PadreTutorRepository padreTutorRepository;

    public List<PerfilNino> obtenerPorPadre(Integer padreId) {
        return perfilNinoRepository.findByPadreId(padreId);
    }

    public PerfilNino obtenerPorId(Integer id) {
        return perfilNinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + id));
    }

    @Transactional
    public PerfilNino crear(Integer padreId, PerfilNino perfil) {
        PadreTutor padre = padreTutorRepository.findById(padreId)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado: " + padreId));
        perfil.setPadre(padre);
        return perfilNinoRepository.save(perfil);
    }

    @Transactional
    public PerfilNino actualizar(Integer id, PerfilNino datos) {
        PerfilNino perfil = obtenerPorId(id);
        perfil.setNombre(datos.getNombre());
        perfil.setEdad(datos.getEdad());
        perfil.setDiagnostico(datos.getDiagnostico());
        perfil.setAvatar(datos.getAvatar());
        return perfilNinoRepository.save(perfil);
    }

    public void eliminar(Integer id) {
        perfilNinoRepository.deleteById(id);
    }
}
