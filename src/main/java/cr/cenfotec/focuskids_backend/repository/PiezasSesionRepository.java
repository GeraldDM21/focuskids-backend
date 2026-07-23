package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.PiezasSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PiezasSesionRepository extends JpaRepository<PiezasSesion, Long> {
    List<PiezasSesion> findByPerfilIdOrderByCreadoEnDesc(Integer perfilId);
}
