package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.LogAuditoria;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.LogAuditoriaRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
