package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.EventoCalendarioRequest;
import cr.cenfotec.focuskids_backend.dto.EventoCalendarioResponse;
import cr.cenfotec.focuskids_backend.model.Docente;
import cr.cenfotec.focuskids_backend.model.EventoCalendario;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.TipoEventoCalendario;
import cr.cenfotec.focuskids_backend.repository.AsignacionRepository;
import cr.cenfotec.focuskids_backend.repository.DocenteRepository;
import cr.cenfotec.focuskids_backend.repository.EventoCalendarioRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoCalendarioService {

    private final EventoCalendarioRepository eventoCalendarioRepository;
    private final AsignacionRepository       asignacionRepository;
    private final DocenteRepository          docenteRepository;
    private final PerfilNinoRepository       perfilNinoRepository;

    @Transactional
    public EventoCalendario crear(Integer docenteUsuarioId, EventoCalendarioRequest datos) {
        Docente docente = docenteRepository.findByUsuarioId(docenteUsuarioId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        EventoCalendario evento = EventoCalendario.builder()
                .docente(docente)
                .perfil(resolverPerfil(datos.perfilId()))
                .tipo(TipoEventoCalendario.valueOf(datos.tipo()))
                .titulo(datos.titulo())
                .descripcion(datos.descripcion())
                .fecha(datos.fecha())
                .hora(datos.hora())
                .build();

        return eventoCalendarioRepository.save(evento);
    }

    @Transactional
    public EventoCalendario actualizar(Integer id, EventoCalendarioRequest datos) {
        EventoCalendario evento = eventoCalendarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado: " + id));

        evento.setPerfil(resolverPerfil(datos.perfilId()));
        evento.setTipo(TipoEventoCalendario.valueOf(datos.tipo()));
        evento.setTitulo(datos.titulo());
        evento.setDescripcion(datos.descripcion());
        evento.setFecha(datos.fecha());
        evento.setHora(datos.hora());

        return eventoCalendarioRepository.save(evento);
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
                .forEach(a -> resultado.add(new EventoCalendarioResponse(
                        a.getId(),
                        "ASIGNACION",
                        "ASIGNACION",
                        a.getTitulo(),
                        a.getDescripcion(),
                        a.getFechaLimite(),
                        null,
                        null,
                        null
                )));

        resultado.sort((a, b) -> a.fecha().compareTo(b.fecha()));
        return resultado;
    }

    private PerfilNino resolverPerfil(Integer perfilId) {
        if (perfilId == null) return null;
        return perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilId));
    }
}
