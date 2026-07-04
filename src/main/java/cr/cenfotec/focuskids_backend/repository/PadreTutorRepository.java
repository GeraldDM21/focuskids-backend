package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.PadreTutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PadreTutorRepository extends JpaRepository<PadreTutor, Integer> {
    Optional<PadreTutor> findByUsuarioId(Integer usuarioId);
}
