package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.LogAuditoria;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.LogAuditoriaRepository;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio centralizado para registrar entradas inmutables en log_auditoria.
 * Puede ser inyectado desde cualquier controller o service.
 *
 * Tipos de accion predefinidos (CA-01):
 *   USUARIO_MODIFICADO, USUARIO_ACTIVADO, USUARIO_DESACTIVADO,
 *   JUEGO_DESACTIVADO, CONFIG_NIVEL_CAMBIADA,
 *   LOGIN_EXITOSO, LOGIN_FALLIDO,
 *   PERFIL_CREADO, PERFIL_ELIMINADO
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditoriaService {

    private final LogAuditoriaRepository logRepo;
    private final UsuarioRepository      usuarioRepo;

    // ── Constantes de tipo de acción ──────────────────────────────────────────
    public static final String USUARIO_MODIFICADO   = "USUARIO_MODIFICADO";
    public static final String USUARIO_ACTIVADO     = "USUARIO_ACTIVADO";
    public static final String USUARIO_DESACTIVADO  = "USUARIO_DESACTIVADO";
    public static final String JUEGO_DESACTIVADO    = "JUEGO_DESACTIVADO";
    public static final String CONFIG_NIVEL_CAMBIADA = "CONFIG_NIVEL_CAMBIADA";
    public static final String LOGIN_EXITOSO        = "LOGIN_EXITOSO";
    public static final String LOGIN_FALLIDO        = "LOGIN_FALLIDO";
    public static final String PERFIL_CREADO        = "PERFIL_CREADO";
    public static final String PERFIL_ELIMINADO     = "PERFIL_ELIMINADO";
    public static final String SESION_ELIMINADA     = "SESION_ELIMINADA";

    public static final String EXITO = "EXITO";
    public static final String FALLO = "FALLO";

    // ── API principal ─────────────────────────────────────────────────────────

    public void registrar(Integer usuarioId, String accion, String entidad,
                          String descripcion, String ip, String resultado) {
        try {
            Usuario usuario = usuarioId != null
                    ? usuarioRepo.findById(usuarioId).orElse(null)
                    : null;

            LogAuditoria log = LogAuditoria.builder()
                    .usuario(usuario)
                    .accion(accion)
                    .entidad(entidad)
                    .descripcion(descripcion)
                    .ip(ip)
                    .resultado(resultado != null ? resultado : EXITO)
                    .fecha(LocalDateTime.now())
                    .build();

            logRepo.save(log);
        } catch (Exception e) {
            // Nunca dejar que un fallo de auditoría rompa la operación principal
            log.error("Error registrando log de auditoría: {}", e.getMessage());
        }
    }

    /** Sobrecarga sin entidad (para acciones genéricas). */
    public void registrar(Integer usuarioId, String accion,
                          String descripcion, String ip, String resultado) {
        registrar(usuarioId, accion, null, descripcion, ip, resultado);
    }
}
