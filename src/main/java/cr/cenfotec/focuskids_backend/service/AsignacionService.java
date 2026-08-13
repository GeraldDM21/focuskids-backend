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
    private final NotificacionService         notificacionService;

    /**
     * Crea una asignación. Si perfilId es null se distribuye a todos los alumnos activos
     * del docente; si viene, se asigna únicamente a ese alumno (debe pertenecer a su clase).
     * En ambos casos se notifica al padre de cada alumno afectado.
     */
    @Transactional
    public Asignacion crear(Integer docenteUsuarioId, Asignacion datos, Integer perfilId) {
        Docente docente = docenteRepository.findByUsuarioId(docenteUsuarioId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        if (datos.getFechaLimite() != null && datos.getFechaLimite().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha límite no puede ser anterior a hoy.");
        }

        datos.setDocente(docente);
        datos.setCantidadAlumnos(null);
        datos.setAlumnoNombre(null);
        Asignacion guardada = asignacionRepository.save(datos);

        List<PerfilNino> alumnos;
        if (perfilId != null) {
            PerfilNino p = perfilNinoRepository.findById(perfilId)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + perfilId));
            if (p.getDocente() == null || !p.getDocente().getUsuario().getId().equals(docenteUsuarioId)) {
                throw new RuntimeException("Ese alumno no pertenece a tu clase.");
            }
            alumnos = List.of(p);
        } else {
            alumnos = perfilNinoRepository.findByDocenteUsuarioId(docenteUsuarioId);
        }

        String docenteEmail = docente.getUsuario().getEmail();
        for (PerfilNino p : alumnos) {
            AsignacionPerfil ap = AsignacionPerfil.builder()
                    .asignacion(guardada)
                    .perfil(p)
                    .sesionesCompletadas(0)
                    .completada(false)
                    .build();
            asignacionPerfilRepository.save(ap);

            Integer padreUsuarioId = p.getPadre().getUsuario().getId();
            String mensaje = "Nueva asignación para " + p.getNombre() + ": " + guardada.getTitulo();
            notificacionService.crearDetallada(
                    padreUsuarioId, "ASIGNACION", guardada.getTitulo(), mensaje,
                    guardada.getDescripcion(), guardada.getFechaLimite(), null, docenteEmail
            );
        }

        guardada.setCantidadAlumnos(alumnos.size());
        if (alumnos.size() == 1) guardada.setAlumnoNombre(alumnos.get(0).getNombre());
        return guardada;
    }

    public List<Asignacion> listarPorDocente(Integer docenteUsuarioId) {
        List<Asignacion> lista = asignacionRepository.findByDocenteUsuarioId(docenteUsuarioId);
        lista.forEach(a -> {
            List<AsignacionPerfil> asignados = asignacionPerfilRepository.findByAsignacionId(a.getId());
            a.setCantidadAlumnos(asignados.size());
            if (asignados.size() == 1) a.setAlumnoNombre(asignados.get(0).getPerfil().getNombre());
        });
        return lista;
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
        if (nuevaFecha.isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha límite no puede ser anterior a hoy.");
        }
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
