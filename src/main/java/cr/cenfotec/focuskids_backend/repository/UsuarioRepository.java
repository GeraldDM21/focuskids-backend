package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.model.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByRol(UsuarioRol rol);
    Optional<Usuario> findByTokenVerificacion(String tokenVerificacion);
}
