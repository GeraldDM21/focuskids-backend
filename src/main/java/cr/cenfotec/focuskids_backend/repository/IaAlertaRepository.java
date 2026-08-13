package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.EstadoAlerta;
import cr.cenfotec.focuskids_backend.model.IaAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IaAlertaRepository extends JpaRepository<IaAlerta, Integer> {

    boolean existsByPerfilIdAndJuegoIdAndEstadoAndFechaEnvioAfter(
            Integer perfilId, Integer juegoId, EstadoAlerta estado, LocalDateTime desde);

    List<IaAlerta> findByPerfilIdOrderByFechaEnvioDesc(Integer perfilId);
}