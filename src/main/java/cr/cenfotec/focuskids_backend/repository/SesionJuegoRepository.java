package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.SesionJuego;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SesionJuegoRepository extends JpaRepository<SesionJuego, Integer> {
    List<SesionJuego> findByPerfilIdOrderByInicioDesc(Integer perfilId);
    List<SesionJuego> findByPerfilIdAndJuegoId(Integer perfilId, Integer juegoId);
    List<SesionJuego> findByPerfilIdAndCompletada(Integer perfilId, Boolean completada);
    List<SesionJuego> findByPerfilIdAndInicioAfter(Integer perfilId, LocalDateTime inicio);
    List<SesionJuego> findByPerfilIdAndInicioAfterOrderByInicioDesc(Integer perfilId, LocalDateTime inicio);

    // Motor de IA / CA-01 y CA-02: historial cronológico de sesiones válidas
    // de un niño, en un juego y nivel específicos.
    List<SesionJuego> findByPerfilIdAndJuegoIdAndNivelIdAndSesionValidaTrueOrderByFinAsc(
            Integer perfilId, Integer juegoId, Integer nivelId);

    // ── RF-Historial: CA-01/CA-02/CA-03 — historial detallado con filtros
    // combinables (juego, nivel, rango de fechas) y paginación. Mismo patrón
    // que LogAuditoriaRepository#filtrar (parámetros opcionales vía IS NULL OR).
    @Query("""
        SELECT s FROM SesionJuego s
        WHERE s.perfil.id = :perfilId
          AND (:juegoId         IS NULL OR s.juego.id = :juegoId)
          AND (:nivel           IS NULL OR s.nivel.nivel = :nivel)
          AND (:fechaDesde      IS NULL OR s.inicio >= :fechaDesde)
          AND (:fechaHasta      IS NULL OR s.inicio <= :fechaHasta)
          AND (:soloCompletadas IS NULL OR s.completada = :soloCompletadas)
        ORDER BY s.inicio DESC
        """)
    Page<SesionJuego> filtrarHistorial(
        @Param("perfilId")        Integer perfilId,
        @Param("juegoId")         Integer juegoId,
        @Param("nivel")           String nivel,
        @Param("fechaDesde")      LocalDateTime fechaDesde,
        @Param("fechaHasta")      LocalDateTime fechaHasta,
        @Param("soloCompletadas") Boolean soloCompletadas,
        Pageable pageable
    );

    /** Misma lógica sin paginación, para exportar el PDF del filtro actual (CA-05). */
    @Query("""
        SELECT s FROM SesionJuego s
        WHERE s.perfil.id = :perfilId
          AND (:juegoId         IS NULL OR s.juego.id = :juegoId)
          AND (:nivel           IS NULL OR s.nivel.nivel = :nivel)
          AND (:fechaDesde      IS NULL OR s.inicio >= :fechaDesde)
          AND (:fechaHasta      IS NULL OR s.inicio <= :fechaHasta)
          AND (:soloCompletadas IS NULL OR s.completada = :soloCompletadas)
        ORDER BY s.inicio DESC
        """)
    List<SesionJuego> filtrarHistorialSinPaginacion(
        @Param("perfilId")        Integer perfilId,
        @Param("juegoId")         Integer juegoId,
        @Param("nivel")           String nivel,
        @Param("fechaDesde")      LocalDateTime fechaDesde,
        @Param("fechaHasta")      LocalDateTime fechaHasta,
        @Param("soloCompletadas") Boolean soloCompletadas
    );

    // CA-04: sesión inmediatamente anterior (mismo perfil + juego) para la comparación al expandir.
    Optional<SesionJuego> findFirstByPerfilIdAndJuegoIdAndInicioLessThanOrderByInicioDesc(
        Integer perfilId, Integer juegoId, LocalDateTime inicio
    );
}
