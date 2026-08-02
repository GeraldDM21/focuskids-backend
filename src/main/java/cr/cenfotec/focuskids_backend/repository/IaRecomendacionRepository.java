package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.IaRecomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IaRecomendacionRepository extends JpaRepository<IaRecomendacion, Integer> {

    /** Recomendación vigente (más reciente) para un niño + juego. */
    Optional<IaRecomendacion> findTopByPerfilIdAndJuegoIdOrderByFechaRecomendacionDesc(
            Integer perfilId, Integer juegoId);

    /** Historial completo de recomendaciones para un niño + juego. */
    List<IaRecomendacion> findByPerfilIdAndJuegoIdOrderByFechaRecomendacionDesc(
            Integer perfilId, Integer juegoId);

    /** Todas las recomendaciones vigentes de un niño (una por juego, la más reciente). */
    List<IaRecomendacion> findByPerfilId(Integer perfilId);
}
