package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Integer> {
    List<Recomendacion> findByPerfilIdOrderByFechaDesc(Integer perfilId);
    List<Recomendacion> findByPerfilIdAndAplicada(Integer perfilId, Boolean aplicada);
}
