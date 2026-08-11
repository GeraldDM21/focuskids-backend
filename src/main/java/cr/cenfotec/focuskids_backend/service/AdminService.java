package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.UsuarioEditRequest;
import cr.cenfotec.focuskids_backend.model.LogAuditoria;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.model.UsuarioRol;
import cr.cenfotec.focuskids_backend.repository.LogAuditoriaRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsuarioRepository      usuarioRepository;
    private final PerfilNinoRepository   perfilNinoRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final AuditoriaService       auditoriaService;

    private static final int    PAGE_SIZE   = 25;
    private static final int    LIMIT_DAYS  = 90;
    private static final DateTimeFormatter CSV_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // ── Usuarios ──────────────────────────────────────────────────────────────

    public Integer getIdPorEmail(String email) {
        return usuarioRepository.findByEmail(email).map(Usuario::getId).orElse(null);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
    }

    @Transactional
    public Usuario toggleActivo(Integer id, String ipOrigen) {
        Usuario usuario = obtenerUsuario(id);
        boolean nuevoEstado = !Boolean.TRUE.equals(usuario.getActivo());
        usuario.setActivo(nuevoEstado);
        Usuario guardado = usuarioRepository.save(usuario);

        String accion = nuevoEstado ? AuditoriaService.USUARIO_ACTIVADO
                                    : AuditoriaService.USUARIO_DESACTIVADO;
        String desc = "%s (ID: %d, email: %s)".formatted(
                nuevoEstado ? "Usuario activado" : "Usuario desactivado",
                id, usuario.getEmail());
        auditoriaService.registrar(null, accion, "Usuario", desc, ipOrigen, AuditoriaService.EXITO);

        return guardado;
    }

    /** Mantiene compatibilidad con el controller existente. */
    @Transactional
    public Usuario toggleActivo(Integer id) {
        return toggleActivo(id, "0.0.0.0");
    }

    public List<PerfilNino> listarNinos() {
        return perfilNinoRepository.findAll();
    }

    // ── Usuarios: búsqueda, edición, eliminación ──────────────────────────────

    /** CA-01: búsqueda paginada por nombre/email y filtro por rol. */
    public Page<Usuario> buscarUsuarios(String q, String rol, int page) {
        UsuarioRol rolEnum = (rol != null && !rol.isBlank())
                ? UsuarioRol.valueOf(rol) : null;
        String query = (q != null && !q.isBlank()) ? q : null;
        return usuarioRepository.buscar(query, rolEnum, PageRequest.of(page, PAGE_SIZE));
    }

    /**
     * CA-02: editar nombre, rol y/o estado de un usuario.
     * CA-05: registra log de auditoría.
     */
    @Transactional
    public Usuario editarUsuario(Integer id, UsuarioEditRequest req, Integer adminId, String ip) {
        Usuario usuario = obtenerUsuario(id);
        StringBuilder cambios = new StringBuilder();

        if (req.nombre() != null && !req.nombre().isBlank()) {
            cambios.append("nombre '%s'→'%s' ".formatted(usuario.getNombre(), req.nombre()));
            usuario.setNombre(req.nombre().trim());
        }
        if (req.rol() != null && !req.rol().isBlank()) {
            UsuarioRol nuevoRol = UsuarioRol.valueOf(req.rol());
            cambios.append("rol '%s'→'%s' ".formatted(usuario.getRol(), nuevoRol));
            usuario.setRol(nuevoRol);
        }
        if (req.activo() != null) {
            cambios.append("activo '%s'→'%s' ".formatted(usuario.getActivo(), req.activo()));
            usuario.setActivo(req.activo());
        }

        Usuario guardado = usuarioRepository.save(usuario);

        auditoriaService.registrar(adminId, AuditoriaService.USUARIO_MODIFICADO,
                "Usuario",
                "Usuario ID %d (%s): %s".formatted(id, usuario.getEmail(), cambios),
                ip, AuditoriaService.EXITO);
        return guardado;
    }

    /**
     * CA-04: eliminar usuario con validación de perfiles activos.
     * CA-05: registra log de auditoría.
     */
    @Transactional
    public void eliminarUsuario(Integer id, Integer adminId, String ip) {
        Usuario usuario = obtenerUsuario(id);

        // No permitir eliminar al último administrador activo
        if (usuario.getRol() == UsuarioRol.ADMINISTRADOR) {
            long adminsActivos = usuarioRepository.countByRolAndActivo(
                    UsuarioRol.ADMINISTRADOR, true);
            if (adminsActivos <= 1) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "No puedes eliminar el único administrador activo del sistema.");
            }
        }

        // CA-04: verificar perfiles activos
        boolean tieneNinosPadre   = usuarioRepository.tieneNinosActivosComoPadre(id);
        boolean tieneNinosDocente = usuarioRepository.tieneNinosActivosComoDocente(id);

        if (tieneNinosPadre || tieneNinosDocente) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Debes reasignar o eliminar los perfiles de niños antes de eliminar esta cuenta.");
        }

        String desc = "Usuario eliminado: ID %d, email %s, rol %s"
                .formatted(id, usuario.getEmail(), usuario.getRol());
        usuarioRepository.delete(usuario);

        auditoriaService.registrar(adminId, "USUARIO_ELIMINADO",
                "Usuario", desc, ip, AuditoriaService.EXITO);
    }

    // ── Logs (legacy, sin paginación) ─────────────────────────────────────────

    public List<LogAuditoria> obtenerLogs() {
        return logAuditoriaRepository.findAll(
                Sort.by(Sort.Direction.DESC, "fecha"));
    }

    public List<LogAuditoria> obtenerLogsPorUsuario(Integer usuarioId) {
        return logAuditoriaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    // ── Logs paginados con filtros (CA-02, CA-04, CA-05) ─────────────────────

    /**
     * Devuelve logs paginados con filtros opcionales.
     * CA-04: 25 por página, más recientes primero.
     * CA-05: limitado a los últimos 90 días.
     */
    public Page<LogAuditoria> obtenerLogsFiltrados(
            int page,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            String accion,
            Integer usuarioId) {

        LocalDateTime limite90 = LocalDateTime.now().minusDays(LIMIT_DAYS);
        PageRequest pageable   = PageRequest.of(page, PAGE_SIZE);

        return logAuditoriaRepository.filtrar(
                limite90, fechaDesde, fechaHasta,
                accion, usuarioId,
                pageable);
    }

    /**
     * Genera un CSV con los mismos filtros (sin paginación) para exportar.
     * CA-02: "Exportar filtro actual a CSV".
     */
    public byte[] exportarLogsCsv(
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            String accion,
            Integer usuarioId) {

        LocalDateTime limite90 = LocalDateTime.now().minusDays(LIMIT_DAYS);

        List<LogAuditoria> logs = logAuditoriaRepository.filtrarSinPaginacion(
                limite90, fechaDesde, fechaHasta, accion, usuarioId);

        StringBuilder csv = new StringBuilder();
        csv.append("Timestamp,Usuario,Email,Tipo Accion,Descripcion,IP Origen,Resultado\n");

        for (LogAuditoria l : logs) {
            csv.append(escapeCsv(l.getFecha() != null ? l.getFecha().format(CSV_FMT) : "")).append(",");
            csv.append(escapeCsv(l.getUsuario() != null ? l.getUsuario().getNombre() : "Sistema")).append(",");
            csv.append(escapeCsv(l.getUsuario() != null ? l.getUsuario().getEmail()  : "")).append(",");
            csv.append(escapeCsv(l.getAccion())).append(",");
            csv.append(escapeCsv(l.getDescripcion())).append(",");
            csv.append(escapeCsv(l.getIp())).append(",");
            csv.append(escapeCsv(l.getResultado())).append("\n");
        }

        return csv.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
