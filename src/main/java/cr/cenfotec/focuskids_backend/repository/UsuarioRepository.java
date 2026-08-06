package cr.cenfotec.focuskids_backend.repository;

import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.model.UsuarioRol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByRol(UsuarioRol rol);
    Optional<Usuario> findByTokenVerificacion(String tokenVerificacion);

    /** CA-01: búsqueda por nombre o email con filtro de rol opcional. */
    @Query("""
        SELECT u FROM Usuario u
        WHERE (:q IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%',:q,'%'))
                          OR LOWER(u.email)  LIKE LOWER(CONCAT('%',:q,'%')))
          AND (:rol IS NULL OR u.rol = :rol)
        ORDER BY u.fechaCreacion DESC
        """)
    Page<Usuario> buscar(
        @Param("q")   String q,
        @Param("rol") UsuarioRol rol,
        Pageable pageable
    );

    /** CA-04: verificar si el usuario tiene perfiles de niños activos como padre. */
    @Query("""
        SELECT COUNT(p) > 0 FROM PerfilNino p
        WHERE p.padre.usuario.id = :usuarioId AND p.activo = true
        """)
    boolean tieneNinosActivosComoPadre(@Param("usuarioId") Integer usuarioId);

    /** CA-04: verificar si el usuario tiene perfiles de niños activos como docente. */
    @Query("""
        SELECT COUNT(p) > 0 FROM PerfilNino p
        WHERE p.docente.usuario.id = :usuarioId AND p.activo = true
        """)
    boolean tieneNinosActivosComoDocente(@Param("usuarioId") Integer usuarioId);

    /** Cantidad de administradores activos (para no eliminar el último). */
    long countByRolAndActivo(UsuarioRol rol, Boolean activo);
}
