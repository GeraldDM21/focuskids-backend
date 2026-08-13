package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.EventoCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoCalendarioRepository extends JpaRepository<EventoCalendario, Integer> {
    List<EventoCalendario> findByDocenteUsuarioIdAndFechaBetween(Integer usuarioId, LocalDate desde, LocalDate hasta);
}
