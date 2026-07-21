package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.CalificacionDocente;
import cr.cenfotec.focuskids_backend.model.Docente;
import cr.cenfotec.focuskids_backend.model.PadreTutor;
import cr.cenfotec.focuskids_backend.repository.CalificacionDocenteRepository;
import cr.cenfotec.focuskids_backend.repository.DocenteRepository;
import cr.cenfotec.focuskids_backend.repository.PadreTutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalificacionDocenteService {

    private final CalificacionDocenteRepository calificacionRepository;
    private final DocenteRepository             docenteRepository;
    private final PadreTutorRepository          padreTutorRepository;

    public List<CalificacionDocente> listarPorDocente(Integer docenteId) {
        return calificacionRepository.findByDocenteId(docenteId);
    }

    public Map<String, Object> resumenDocente(Integer docenteId) {
        double promedio = calificacionRepository.promedioByDocenteId(docenteId);
        long total = calificacionRepository.findByDocenteId(docenteId).size();
        return Map.of("promedio", Math.round(promedio * 10.0) / 10.0, "total", total);
    }

    @Transactional
    public CalificacionDocente calificar(Integer padreUsuarioId, Integer docenteId, Integer puntuacion, String comentario) {
        if (puntuacion < 1 || puntuacion > 5) throw new RuntimeException("Puntuación debe ser entre 1 y 5");

        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado: " + docenteId));
        PadreTutor padre = padreTutorRepository.findByUsuarioId(padreUsuarioId)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado"));

        // Upsert: si ya calificó, actualiza
        CalificacionDocente cal = calificacionRepository
                .findByPadreIdAndDocenteId(padre.getId(), docenteId)
                .orElse(CalificacionDocente.builder().padre(padre).docente(docente).build());

        cal.setPuntuacion(puntuacion);
        cal.setComentario(comentario);
        return calificacionRepository.save(cal);
    }
}
