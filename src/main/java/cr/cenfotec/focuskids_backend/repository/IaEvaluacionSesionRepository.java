package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.IaEvaluacionSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IaEvaluacionSesionRepository extends JpaRepository<IaEvaluacionSesion, Integer> {

    // CA-05: todas las evaluaciones de un niño+juego (para quedarnos con la
    // más reciente por nivel en el endpoint GET).
    List<IaEvaluacionSesion> findByNinoPerfilIdAndJuegoIdOrderByFechaEvaluacionDesc(
            Integer ninoPerfilId, Integer juegoId);

    Optional<IaEvaluacionSesion> findFirstByNinoPerfilIdAndJuegoIdAndNivelOrderByFechaEvaluacionDesc(
            Integer ninoPerfilId, Integer juegoId, String nivel);
}
