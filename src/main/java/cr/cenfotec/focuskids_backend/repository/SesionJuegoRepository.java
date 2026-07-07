package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.SesionJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SesionJuegoRepository extends JpaRepository<SesionJuego, Integer> {
    List<SesionJuego> findByPerfilIdOrderByInicioDesc(Integer perfilId);
    List<SesionJuego> findByPerfilIdAndJuegoId(Integer perfilId, Integer juegoId);
    List<SesionJuego> findByPerfilIdAndCompletada(Integer perfilId, Boolean completada);
}
