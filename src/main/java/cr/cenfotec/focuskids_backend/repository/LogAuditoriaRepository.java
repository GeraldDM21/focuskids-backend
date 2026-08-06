package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.LogAuditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Integer> {

    List<LogAuditoria> findByUsuarioIdOrderByFechaDesc(Integer usuarioId);
    List<LogAuditoria> findByEntidad(String entidad);

    /**
     * CA-02: Filtros opcionales — fecha, tipo acción, usuario.
     * CA-04: Pageable gestiona los 25 por página, orden DESC ya en Pageable.
     * CA-05: limite90 garantiza que sólo se devuelven los últimos 90 días.
     */
    @Query("""
        SELECT l FROM LogAuditoria l
        WHERE l.fecha >= :limite90
          AND (:fechaDesde IS NULL OR l.fecha >= :fechaDesde)
          AND (:fechaHasta IS NULL OR l.fecha <= :fechaHasta)
          AND (:accion     IS NULL OR l.accion = :accion)
          AND (:usuarioId  IS NULL OR l.usuario.id = :usuarioId)
        ORDER BY l.fecha DESC
        """)
    Page<LogAuditoria> filtrar(
        @Param("limite90")   LocalDateTime limite90,
        @Param("fechaDesde") LocalDateTime fechaDesde,
        @Param("fechaHasta") LocalDateTime fechaHasta,
        @Param("accion")     String accion,
        @Param("usuarioId")  Integer usuarioId,
        Pageable pageable
    );

    /** Misma lógica sin paginación, para exportar CSV. */
    @Query("""
        SELECT l FROM LogAuditoria l
        WHERE l.fecha >= :limite90
          AND (:fechaDesde IS NULL OR l.fecha >= :fechaDesde)
          AND (:fechaHasta IS NULL OR l.fecha <= :fechaHasta)
          AND (:accion     IS NULL OR l.accion = :accion)
          AND (:usuarioId  IS NULL OR l.usuario.id = :usuarioId)
        ORDER BY l.fecha DESC
        """)
    List<LogAuditoria> filtrarSinPaginacion(
        @Param("limite90")   LocalDateTime limite90,
        @Param("fechaDesde") LocalDateTime fechaDesde,
        @Param("fechaHasta") LocalDateTime fechaHasta,
        @Param("accion")     String accion,
        @Param("usuarioId")  Integer usuarioId
    );
}
