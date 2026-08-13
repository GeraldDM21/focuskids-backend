package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.EventoCalendarioRequest;
import cr.cenfotec.focuskids_backend.dto.EventoCalendarioResponse;
import cr.cenfotec.focuskids_backend.model.Docente;
import cr.cenfotec.focuskids_backend.model.EventoCalendario;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.TipoEventoCalendario;
import cr.cenfotec.focuskids_backend.repository.AsignacionPerfilRepository;
import cr.cenfotec.focuskids_backend.repository.AsignacionRepository;
import cr.cenfotec.focuskids_backend.repository.DocenteRepository;
import cr.cenfotec.focuskids_backend.repository.EventoCalendarioRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventoCalendarioService {

    private final EventoCalendarioRepository eventoCalendarioRepository;
    private final AsignacionRepository       asignacionRepository;
    private final AsignacionPerfilRepository asignacionPerfilRepository;
    private final DocenteRepository          docenteRepository;
    private final PerfilNinoRepository       perfilNinoRepository;
    private final NotificacionService        notificacionService;

    @Transactional
    public EventoCalendario crear(Integer docenteUsuarioId, EventoCalendarioRequest datos) {
        Docente docente = docenteRepository.findByUsuarioId(docenteUsuarioId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        TipoEventoCalendario tipo = TipoEventoCalendario.valueOf(datos.tipo());
        validarFechaFutura(datos.fecha(), datos.hora());
        validarSinChoque(docenteUsuarioId, tipo, datos.fecha(), datos.hora(), null);

        PerfilNino perfil = resolverPerfil(datos.perfilId());

        EventoCalendario evento = EventoCalendario.builder()
                .docente(docente)
                .perfil(perfil)
                .tipo(tipo)
                .titulo(datos.titulo())
                .descripcion(datos.descripcion())
                .fecha(datos.fecha())
                .hora(datos.hora())
                .build();

        EventoCalendario guardado = eventoCalendarioRepository.save(evento);

        String docenteEmail = docente.getUsuario().getEmail();
        if (perfil != null) {
            // Vinculado a un alumno específico: avisamos solo a su padre.
            Integer padreUsuarioId = perfil.getPadre().getUsuario().getId();
            String mensaje = (tipo == TipoEventoCalendario.CITA ? "Nueva cita" : "Nuevo recordatorio")
                    + " para " + perfil.getNombre() + ": " + guardado.getTitulo();
            notificacionService.crearDetallada(
                    padreUsuarioId, tipo.name(), guardado.getTitulo(), mensaje,
                    guardado.getDescripcion(), guardado.getFecha(), guardado.getHora(), docenteEmail
            );
        } else {
            // Evento general (toda la clase): avisamos a los padres de todos los alumnos del docente,
            // sin duplicar si un mismo padre tiene más de un hijo con este docente.
            List<PerfilNino> alumnos = perfilNinoRepository.findByDocenteUsuarioId(docenteUsuarioId);
            Set<Integer> padresNotificados = new HashSet<>();
            String mensaje = (tipo == TipoEventoCalendario.CITA ? "Nueva cita: " : "Nuevo recordatorio: ") + guardado.getTitulo();
            for (PerfilNino p : alumnos) {
                Integer padreUsuarioId = p.getPadre().getUsuario().getId();
                if (!padresNotificados.add(padreUsuarioId)) continue;
                notificacionService.crearDetallada(
                        padreUsuarioId, tipo.name(), guardado.getTitulo(), mensaje,
                        guardado.getDescripcion(), guardado.getFecha(), guardado.getHora(), docenteEmail
                );
            }
        }

        return guardado;
    }

    @Transactional
    public EventoCalendario actualizar(Integer id, EventoCalendarioRequest datos) {
        EventoCalendario evento = eventoCalendarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado: " + id));

        Integer docenteUsuarioId = evento.getDocente().getUsuario().getId();
        TipoEventoCalendario tipo = TipoEventoCalendario.valueOf(datos.tipo());
        validarFechaFutura(datos.fecha(), datos.hora());
        validarSinChoque(docenteUsuarioId, tipo, datos.fecha(), datos.hora(), id);

        evento.setPerfil(resolverPerfil(datos.perfilId()));
        evento.setTipo(tipo);
        evento.setTitulo(datos.titulo());
        evento.setDescripcion(datos.descripcion());
        evento.setFecha(datos.fecha());
        evento.setHora(datos.hora());

        return eventoCalendarioRepository.save(evento);
    }

    /** No se permite agendar citas ni recordatorios en fechas/horas que ya pasaron. */
    private void validarFechaFutura(LocalDate fecha, LocalTime hora) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime momento = hora != null ? LocalDateTime.of(fecha, hora) : LocalDateTime.of(fecha, LocalTime.MIN);
        if (hora != null && momento.isBefore(ahora)) {
            throw new RuntimeException("No se pueden agendar eventos en una fecha y hora que ya pasó.");
        }
        if (hora == null && fecha.isBefore(ahora.toLocalDate())) {
            throw new RuntimeException("No se pueden agendar eventos en una fecha que ya pasó.");
        }
    }

    /** No se permiten dos citas del mismo docente el mismo día a la misma hora. */
    private void validarSinChoque(Integer docenteUsuarioId, TipoEventoCalendario tipo, LocalDate fecha, LocalTime hora, Integer excluirId) {
        if (tipo != TipoEventoCalendario.CITA || hora == null) return;
        boolean choque = eventoCalendarioRepository
                .findByDocenteUsuarioIdAndTipoAndFechaAndHora(docenteUsuarioId, TipoEventoCalendario.CITA, fecha, hora)
                .stream()
                .anyMatch(e -> excluirId == null || !e.getId().equals(excluirId));
        if (choque) {
            throw new RuntimeException("Ya existe una cita programada para ese día y hora. Elige otro horario.");
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        eventoCalendarioRepository.deleteById(id);
    }

    /**
     * Calendario combinado del docente en un rango de fechas: sus eventos propios
     * (citas/recordatorios) más las asignaciones de su clase, tomadas directo de
     * su fecha límite (no se duplican datos, solo se combinan para la vista).
     */
    public List<EventoCalendarioResponse> listarCalendario(Integer docenteUsuarioId, LocalDate desde, LocalDate hasta) {
        List<EventoCalendarioResponse> resultado = new ArrayList<>();

        eventoCalendarioRepository.findByDocenteUsuarioIdAndFechaBetween(docenteUsuarioId, desde, hasta)
                .forEach(e -> resultado.add(new EventoCalendarioResponse(
                        e.getId(),
                        "EVENTO",
                        e.getTipo().name(),
                        e.getTitulo(),
                        e.getDescripcion(),
                        e.getFecha(),
                        e.getHora(),
                        e.getPerfil() != null ? e.getPerfil().getId() : null,
                        e.getPerfil() != null ? e.getPerfil().getNombre() : null
                )));

        asignacionRepository.findByDocenteUsuarioIdAndFechaLimiteBetween(docenteUsuarioId, desde, hasta)
                .forEach(a -> {
                    // Si la asignación se le dio a un solo alumno, mostramos su nombre en el calendario.
                    var asignados = asignacionPerfilRepository.findByAsignacionId(a.getId());
                    Integer perfilId = asignados.size() == 1 ? asignados.get(0).getPerfil().getId() : null;
                    String perfilNombre = asignados.size() == 1 ? asignados.get(0).getPerfil().getNombre() : null;
                    resultado.add(new EventoCalendarioResponse(
                            a.getId(),
                            "ASIGNACION",
                            "ASIGNACION",
                            a.getTitulo(),
                            a.getDescripcion(),
                            a.getFechaLimite(),
                            null,
                            perfilId,
                            perfilNombre
                    ));
                });

        resultado.sort((a, b) -> a.fecha().compareTo(b.fecha()));
        return resultado;
    }

    private PerfilNino resolverPerfil(Integer perfilId) {
        if (perfilId == null) return null;
        return perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilId));
    }
}
