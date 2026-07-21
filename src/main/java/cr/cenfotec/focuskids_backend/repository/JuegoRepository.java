package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {
    List<Juego> findByActivo(Boolean activo);
    List<Juego> findByTipo(String tipo);
    Optional<Juego> findByNombreIgnoreCase(String nombre);
}
