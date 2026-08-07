package cr.cenfotec.focuskids_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Log inmutable de cambios en la configuración de niveles.
 *
 * IMPORTANTE (CA-05): esta tabla tiene permisos INSERT únicamente para el
 * usuario de base de datos de la aplicación. Nunca se deben exponer métodos
 * update/delete en el repositorio ni en ningún service.
 *
 * Campos: 9 (id, nivelId, juegoId, adminUsuarioId, parametrosAnteriores,
 *             parametrosNuevos, ipOrigen, creadoEn, versionNueva)
 */
@Entity
@Table(name = "admin_config_audit")
@Getter          // solo lectura: no @Setter para reforzar inmutabilidad
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA necesita constructor vacío
@AllArgsConstructor
@Builder
public class AdminConfigAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Campo 2: nivel que fue modificado
    @Column(name = "nivel_id", nullable = false)
    private Integer nivelId;

    // Campo 3: juego al que pertenece el nivel (redundante pero útil para queries)
    @Column(name = "juego_id", nullable = false)
    private Integer juegoId;

    // Campo 4: usuario administrador que hizo el cambio
    @Column(name = "admin_usuario_id", nullable = false)
    private Integer adminUsuarioId;

    // Campo 5: configuración anterior (JSON snapshot)
    @Column(name = "parametros_anteriores", columnDefinition = "TEXT", nullable = false)
    private String parametrosAnteriores;

    // Campo 6: nueva configuración aplicada (JSON snapshot)
    @Column(name = "parametros_nuevos", columnDefinition = "TEXT", nullable = false)
    private String parametrosNuevos;

    // Campo 7: IP del cliente que realizó la solicitud
    @Column(name = "ip_origen", length = 45, nullable = false)  // 45 = IPv6 máx
    private String ipOrigen;

    // Campo 8: timestamp con zona horaria del servidor
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    // Campo 9: identificador de versión generado al guardar
    @Column(name = "version_nueva", length = 50, nullable = false)
    private String versionNueva;

    @PrePersist
    protected void onCreate() {
        if (creadoEn == null) creadoEn = LocalDateTime.now();
    }
}
