package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.PerfilNino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilNinoRepository extends JpaRepository<PerfilNino, Integer> {
    List<PerfilNino> findByPadreId(Integer padreId);
}
