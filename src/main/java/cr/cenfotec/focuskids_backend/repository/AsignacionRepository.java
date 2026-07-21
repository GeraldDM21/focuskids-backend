package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Integer> {
    List<Asignacion> findByDocenteUsuarioId(Integer usuarioId);
    List<Asignacion> findByDocenteId(Integer docenteId);
}
