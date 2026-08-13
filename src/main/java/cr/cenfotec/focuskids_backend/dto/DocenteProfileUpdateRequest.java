package cr.cenfotec.focuskids_backend.dto;

/** Auto-servicio: el docente edita su propio perfil. */
public record DocenteProfileUpdateRequest(
        String nombre,
        String email,
        String institucion,
        String gradoGrupo
) {}
