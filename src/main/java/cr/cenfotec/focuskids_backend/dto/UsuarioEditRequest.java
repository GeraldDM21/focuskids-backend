package cr.cenfotec.focuskids_backend.dto;

/**
 * CA-02: sólo permite cambiar nombre, rol y estado.
 * La contraseña nunca se edita desde el panel de administración.
 */
public record UsuarioEditRequest(
        String  nombre,   // nullable → no cambiar
        String  rol,      // nullable → no cambiar (NINO | PADRE | DOCENTE | ADMINISTRADOR)
        Boolean activo    // nullable → no cambiar
) {}
