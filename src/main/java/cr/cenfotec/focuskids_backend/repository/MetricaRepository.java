package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.Metrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetricaRepository extends JpaRepository<Metrica, Integer> {
    Optional<Metrica> findBySesionId(Integer sesionId);
    List<Metrica> findBySesionPerfilId(Integer perfilId);
}
