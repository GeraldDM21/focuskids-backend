package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.NivelAsignado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NivelAsignadoRepository extends JpaRepository<NivelAsignado, Integer> {

    Optional<NivelAsignado> findByPerfilIdAndJuegoId(Integer perfilId, Integer juegoId);

    List<NivelAsignado> findByPerfilId(Integer perfilId);

    void deleteByPerfilIdAndJuegoId(Integer perfilId, Integer juegoId);
}
