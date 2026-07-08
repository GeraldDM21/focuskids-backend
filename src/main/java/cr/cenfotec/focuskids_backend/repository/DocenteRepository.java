package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    Optional<Docente> findByUsuarioId(Integer usuarioId);
}
