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

    public List<PerfilNino> obtenerPorPadre(Integer usuarioId) {
        // El frontend envía el usuarioId del JWT, no el PadreTutor.id
        return perfilNinoRepository.findByPadreUsuarioId(usuarioId);
    }

    public PerfilNino obtenerPorId(Integer id) {
        return perfilNinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + id));
    }

    @Transactional
    public PerfilNino crear(Integer usuarioId, PerfilNino perfil) {
        // El frontend envía el usuarioId del JWT, no el PadreTutor.id
        PadreTutor padre = padreTutorRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado para usuario: " + usuarioId));
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

    @Transactional
    public PerfilNino toggleActivo(Integer id) {
        PerfilNino perfil = obtenerPorId(id);
        perfil.setActivo(!perfil.getActivo());
        return perfilNinoRepository.save(perfil);
    }

    // CA-05: persistir el nivel de volumen de efectos de sonido (0/25/50/75/100)
    private static final List<Integer> NIVELES_VOLUMEN_VALIDOS = List.of(0, 25, 50, 75, 100);

    @Transactional
    public PerfilNino actualizarVolumen(Integer id, Integer volumen) {
        if (volumen == null || !NIVELES_VOLUMEN_VALIDOS.contains(volumen)) {
            throw new IllegalArgumentException("Nivel de volumen inválido: " + volumen + ". Valores permitidos: " + NIVELES_VOLUMEN_VALIDOS);
        }
        PerfilNino perfil = obtenerPorId(id);
        perfil.setVolumen(volumen);
        return perfilNinoRepository.save(perfil);
    }

    public void eliminar(Integer id) {
        perfilNinoRepository.deleteById(id);
    }
}
