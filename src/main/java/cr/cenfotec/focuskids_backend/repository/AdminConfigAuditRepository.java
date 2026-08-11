package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.AdminConfigAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de solo-lectura + INSERT para admin_config_audit.
 *
 * CA-05: la tabla tiene permisos INSERT pero NO UPDATE/DELETE para el usuario
 * de BD de la aplicación. Este repositorio no expone ningún método de
 * modificación: deleteById(), deleteAll() y save() con entidad existente
 * deben NUNCA ser llamados desde código de producción.
 *
 * El método save() de JpaRepository se usa ÚNICAMENTE para insertar registros
 * nuevos (id = null). El acceso UPDATE/DELETE queda bloqueado a nivel de BD.
 */
@Repository
public interface AdminConfigAuditRepository extends JpaRepository<AdminConfigAudit, Integer> {

    /** Historial completo de un nivel, ordenado del más reciente al más antiguo. */
    List<AdminConfigAudit> findByNivelIdOrderByCreadoEnDesc(Integer nivelId);

    /** Historial de todos los cambios hechos por un administrador. */
    List<AdminConfigAudit> findByAdminUsuarioIdOrderByCreadoEnDesc(Integer adminUsuarioId);

    /** Últimas N auditorías del sistema (para la vista de logs del panel admin). */
    List<AdminConfigAudit> findTop50ByOrderByCreadoEnDesc();
}
