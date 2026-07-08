package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByUsuarioId(Integer usuarioId);
}
