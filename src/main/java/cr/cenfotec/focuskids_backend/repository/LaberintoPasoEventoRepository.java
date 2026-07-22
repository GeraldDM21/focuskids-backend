package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.LaberintoPasoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaberintoPasoEventoRepository
        extends JpaRepository<LaberintoPasoEvento, Integer> {

    List<LaberintoPasoEvento>
    findBySesionIdOrderByNumeroPasoAsc(Integer sesionId);

    long countBySesionId(Integer sesionId);

    long countBySesionIdAndEsCallejonSinSalidaTrue(Integer sesionId);
}
