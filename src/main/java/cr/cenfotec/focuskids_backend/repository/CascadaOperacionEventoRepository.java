package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.CascadaOperacionEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CascadaOperacionEventoRepository
        extends JpaRepository<CascadaOperacionEvento, Integer> {

    List<CascadaOperacionEvento>
    findBySesionIdOrderByNumeroOperacionAsc(Integer sesionId);

    List<CascadaOperacionEvento>
    findTop5BySesionIdOrderByNumeroOperacionDesc(Integer sesionId);

    long countBySesionId(Integer sesionId);
}