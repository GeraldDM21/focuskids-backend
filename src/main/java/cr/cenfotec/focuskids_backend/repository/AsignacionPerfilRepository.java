package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.AsignacionPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionPerfilRepository extends JpaRepository<AsignacionPerfil, Integer> {
    List<AsignacionPerfil> findByPerfilId(Integer perfilId);
    List<AsignacionPerfil> findByAsignacionId(Integer asignacionId);
    Optional<AsignacionPerfil> findByAsignacionIdAndPerfilId(Integer asignacionId, Integer perfilId);
}
