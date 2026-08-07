package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.MisionReclamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MisionReclamadaRepository extends JpaRepository<MisionReclamada, Integer> {

    Optional<MisionReclamada> findByPerfilIdAndFecha(Integer perfilId, LocalDate fecha);

    List<MisionReclamada> findByPerfilIdOrderByFechaDesc(Integer perfilId);
}
