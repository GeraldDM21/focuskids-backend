package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.NivelDificultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NivelDificultadRepository extends JpaRepository<NivelDificultad, Integer> {
    List<NivelDificultad> findByJuegoId(Integer juegoId);
    Optional<NivelDificultad> findByJuegoIdAndNivel(Integer juegoId, String nivel);
}
