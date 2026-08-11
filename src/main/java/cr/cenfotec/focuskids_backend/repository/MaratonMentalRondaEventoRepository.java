package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.MaratonMentalRondaEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaratonMentalRondaEventoRepository
        extends JpaRepository<MaratonMentalRondaEvento, Integer> {

    List<MaratonMentalRondaEvento> findBySesionIdOrderByIdAsc(Integer sesionId);

    long countBySesionId(Integer sesionId);
}
