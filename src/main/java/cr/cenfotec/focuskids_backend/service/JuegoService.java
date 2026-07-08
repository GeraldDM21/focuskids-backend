package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.Juego;
import cr.cenfotec.focuskids_backend.model.NivelDificultad;
import cr.cenfotec.focuskids_backend.repository.JuegoRepository;
import cr.cenfotec.focuskids_backend.repository.NivelDificultadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JuegoService {

    private final JuegoRepository juegoRepository;
    private final NivelDificultadRepository nivelDificultadRepository;

    public List<Juego> listarActivos() {
        return juegoRepository.findByActivo(true);
    }

    public List<Juego> listarTodos() {
        return juegoRepository.findAll();
    }

    public Juego obtenerPorId(Integer id) {
        return juegoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado: " + id));
    }

    public List<NivelDificultad> obtenerNiveles(Integer juegoId) {
        return nivelDificultadRepository.findByJuegoId(juegoId);
    }

    public Juego guardar(Juego juego) {
        return juegoRepository.save(juego);
    }
}
