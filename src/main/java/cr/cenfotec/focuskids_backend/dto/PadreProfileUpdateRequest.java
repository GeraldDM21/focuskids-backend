package cr.cenfotec.focuskids_backend.dto;

/** Auto-servicio: el padre/tutor edita su propio perfil. */
public record PadreProfileUpdateRequest(
        String nombre,
        String email,
        String telefono,
        String relacionConNino
) {}
