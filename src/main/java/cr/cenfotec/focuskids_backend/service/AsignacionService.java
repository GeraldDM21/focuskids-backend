package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository        asignacionRepository;
    private final AsignacionPerfilRepository  asignacionPerfilRepository;
    private final DocenteRepository           docenteRepository;
    private final PerfilNinoRepository        perfilNinoRepository;

    /** Crea una asignación. Si datos.perfilId viene indicado, se enlaza
     *  únicamente a ese alumno (debe pertenecer a la clase del docente);
     *  si no, se enlaza automáticamente a todos los alumnos del docente
     *  (comportamiento original, para tareas generales de toda la clase). */
    @Transactional
    public Asignacion crear(Integer docenteUsuarioId, Asignacion datos) {
        Docente docente = docenteRepository.findByUsuarioId(docenteUsuarioId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        List<PerfilNino> alumnosClase = perfilNinoRepository.findByDocenteUsuarioId(docenteUsuarioId);

        List<PerfilNino> destinatarios;
        Integer perfilId = datos.getPerfilId();
        datos.setPerfilId(null); // campo de solo-request, no se persiste en la fila de Asignacion
        if (perfilId != null) {
            destinatarios = alumnosClase.stream()
                    .filter(p -> p.getId().equals(perfilId))
                    .toList();
            if (destinatarios.isEmpty()) {
                throw new RuntimeException("El alumno indicado no pertenece a la clase de este docente");
            }
        } else {
            destinatarios = alumnosClase;
        }

        datos.setDocente(docente);
        Asignacion guardada = asignacionRepository.save(datos);

        for (PerfilNino p : destinatarios) {
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

    /** Lista las asignaciones del docente, indicando para cada una a qué
     *  alumno(s) quedó enlazada (para distinguir tareas generales de las
     *  dirigidas a un alumno puntual). */
    @Transactional(readOnly = true)
    public List<Asignacion> listarPorDocente(Integer docenteUsuarioId) {
        List<Asignacion> asignaciones = asignacionRepository.findByDocenteUsuarioId(docenteUsuarioId);
        int totalAlumnosClase = perfilNinoRepository.findByDocenteUsuarioId(docenteUsuarioId).size();
        for (Asignacion a : asignaciones) {
            List<AsignacionPerfil> enlaces = asignacionPerfilRepository.findByAsignacionId(a.getId());
            // Solo se marca como "específica" cuando no cubre a toda la clase actual:
            // así una tarea creada para 1 alumno en una clase de 1 sigue viéndose como
            // dirigida a ese alumno (se muestra el nombre igual), y evitamos falsos
            // "general" si luego se unen más alumnos a la clase.
            List<String> nombres = enlaces.stream().map(ap -> ap.getPerfil().getNombre()).toList();
            if (enlaces.size() < totalAlumnosClase || enlaces.size() == 1) {
                a.setAlumnosAsignados(nombres);
            }
        }
        return asignaciones;
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

    /** Permite mover la fecha límite de una asignación, por ejemplo desde el calendario. */
    @Transactional
    public Asignacion actualizarFecha(Integer id, LocalDate nuevaFecha) {
        Asignacion a = asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignacion no encontrada: " + id));
        a.setFechaLimite(nuevaFecha);
        return asignacionRepository.save(a);
    }

    @Transactional
    public void eliminar(Integer id) {
        asignacionPerfilRepository.findByAsignacionId(id).forEach(ap -> asignacionPerfilRepository.delete(ap));
        asignacionRepository.deleteById(id);
    }
}
