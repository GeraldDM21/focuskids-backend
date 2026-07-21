package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository        asignacionRepository;
    private final AsignacionPerfilRepository  asignacionPerfilRepository;
    private final DocenteRepository           docenteRepository;
    private final PerfilNinoRepository        perfilNinoRepository;

    /** Crea una asignación y la enlaza automáticamente a todos los alumnos del docente. */
    @Transactional
    public Asignacion crear(Integer docenteUsuarioId, Asignacion datos) {
        Docente docente = docenteRepository.findByUsuarioId(docenteUsuarioId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        datos.setDocente(docente);
        Asignacion guardada = asignacionRepository.save(datos);

        // Crear una entrada AsignacionPerfil para cada alumno del docente
        List<PerfilNino> alumnos = perfilNinoRepository.findByDocenteUsuarioId(docenteUsuarioId);
        for (PerfilNino p : alumnos) {
            AsignacionPerfil ap = AsignacionPerfil.builder()
                    .asignacion(guardada)
                    .perfil(p)
                    .sesionesCompletadas(0)
                    .completada(false)
                    .build();
            asignacionPerfilRepository.save(ap);
        }
        return guardada;
    }

    public List<Asignacion> listarPorDocente(Integer docenteUsuarioId) {
        return asignacionRepository.findByDocenteUsuarioId(docenteUsuarioId);
    }

    /** Retorna las asignaciones pendientes de un perfil de niño con su progreso. */
    public List<AsignacionPerfil> listarPorPerfil(Integer perfilId) {
        return asignacionPerfilRepository.findByPerfilId(perfilId);
    }

    /** Incrementa sesiones completadas para un perfil en una asignación. */
    @Transactional
    public AsignacionPerfil registrarProgreso(Integer asignacionId, Integer perfilId) {
        AsignacionPerfil ap = asignacionPerfilRepository
                .findByAsignacionIdAndPerfilId(asignacionId, perfilId)
                .orElseGet(() -> {
                    // Crear si no existe (por si el alumno fue asignado después de crear la tarea)
                    Asignacion a = asignacionRepository.findById(asignacionId)
                            .orElseThrow(() -> new RuntimeException("Asignacion no encontrada"));
                    PerfilNino p = perfilNinoRepository.findById(perfilId)
                            .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
                    return AsignacionPerfil.builder().asignacion(a).perfil(p).sesionesCompletadas(0).completada(false).build();
                });

        ap.setSesionesCompletadas(ap.getSesionesCompletadas() + 1);
        int minimo = ap.getAsignacion().getMinimoSesiones() != null ? ap.getAsignacion().getMinimoSesiones() : 1;
        if (!ap.getCompletada() && ap.getSesionesCompletadas() >= minimo) {
            ap.setCompletada(true);
            ap.setFechaCompletada(LocalDateTime.now());
        }
        return asignacionPerfilRepository.save(ap);
    }

    @Transactional
    public void eliminar(Integer id) {
        asignacionPerfilRepository.findByAsignacionId(id).forEach(ap -> asignacionPerfilRepository.delete(ap));
        asignacionRepository.deleteById(id);
    }
}
