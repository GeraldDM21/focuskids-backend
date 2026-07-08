package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Integer> {
    List<LogAuditoria> findByUsuarioIdOrderByFechaDesc(Integer usuarioId);
    List<LogAuditoria> findByEntidad(String entidad);
}
