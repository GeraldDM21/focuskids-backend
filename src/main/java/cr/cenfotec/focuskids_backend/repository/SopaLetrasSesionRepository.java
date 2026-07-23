package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.SopaLetrasSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SopaLetrasSesionRepository extends JpaRepository<SopaLetrasSesion, Long> {

    // Trae todas las sesiones de un perfil ordenadas de la mas reciente a la mas antigua
    List<SopaLetrasSesion> findByPerfilIdOrderByCreadoEnDesc(Integer perfilId);
}
