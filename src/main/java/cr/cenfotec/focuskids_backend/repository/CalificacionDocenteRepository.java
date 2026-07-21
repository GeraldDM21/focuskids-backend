package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.CalificacionDocente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionDocenteRepository extends JpaRepository<CalificacionDocente, Integer> {
    List<CalificacionDocente> findByDocenteId(Integer docenteId);
    Optional<CalificacionDocente> findByPadreIdAndDocenteId(Integer padreId, Integer docenteId);

    @Query("SELECT COALESCE(AVG(c.puntuacion), 0) FROM CalificacionDocente c WHERE c.docente.id = :docenteId")
    Double promedioByDocenteId(Integer docenteId);
}
