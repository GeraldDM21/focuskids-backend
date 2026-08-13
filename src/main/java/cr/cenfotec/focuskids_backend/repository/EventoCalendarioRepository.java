package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.EventoCalendario;
import cr.cenfotec.focuskids_backend.model.TipoEventoCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EventoCalendarioRepository extends JpaRepository<EventoCalendario, Integer> {
    List<EventoCalendario> findByDocenteUsuarioIdAndFechaBetween(Integer usuarioId, LocalDate desde, LocalDate hasta);

    /** Para detectar choques de horario: citas del mismo docente, mismo día y misma hora. */
    List<EventoCalendario> findByDocenteUsuarioIdAndTipoAndFechaAndHora(
            Integer usuarioId, TipoEventoCalendario tipo, LocalDate fecha, LocalTime hora);
}
