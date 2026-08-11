package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.LabCienciasIntento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabCienciasIntentoRepository
        extends JpaRepository<LabCienciasIntento, Integer> {

    List<LabCienciasIntento> findBySesionIdOrderByIdAsc(
            Integer sesionId
    );

    long countBySesionId(Integer sesionId);
}