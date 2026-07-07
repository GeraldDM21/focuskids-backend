package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.AlertaRegresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRegresionRepository extends JpaRepository<AlertaRegresion, Integer> {
    List<AlertaRegresion> findByPerfilIdOrderByFechaDesc(Integer perfilId);
    List<AlertaRegresion> findByPerfilIdAndVista(Integer perfilId, Boolean vista);
}
