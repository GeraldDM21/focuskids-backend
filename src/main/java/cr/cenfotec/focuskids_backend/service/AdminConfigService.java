package cr.cenfotec.focuskids_backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cr.cenfotec.focuskids_backend.dto.NivelConfigRequest;
import cr.cenfotec.focuskids_backend.dto.NivelConfigResponse;
import cr.cenfotec.focuskids_backend.model.AdminConfigAudit;
import cr.cenfotec.focuskids_backend.model.Juego;
import cr.cenfotec.focuskids_backend.model.NivelDificultad;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.AdminConfigAuditRepository;
import cr.cenfotec.focuskids_backend.repository.JuegoRepository;
import cr.cenfotec.focuskids_backend.repository.NivelDificultadRepository;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminConfigService {

    private final JuegoRepository            juegoRepository;
    private final NivelDificultadRepository  nivelRepository;
    private final AdminConfigAuditRepository auditRepository;
    private final UsuarioRepository          usuarioRepository;
    private final ObjectMapper               objectMapper;

    // Orden canónico de dificultad para comparar niveles (CA-04)
    private static final List<String> ORDEN_NIVELES = List.of("FACIL", "MEDIO", "DIFICIL");

    // ─── LECTURAS ────────────────────────────────────────────────────────────

    /** Devuelve los parámetros actuales de todos los juegos y niveles. */
    public List<NivelConfigResponse> getTodasLasConfiguraciones() {
        return nivelRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /** Devuelve los niveles de un juego específico. */
    public List<NivelConfigResponse> getConfiguracionesPorJuego(Integer juegoId) {
        return nivelRepository.findByJuegoId(juegoId).stream()
                .map(this::toResponse)
                .toList();
    }

    /** Devuelve la configuración de un nivel específico. */
    public NivelConfigResponse getConfiguracion(Integer nivelId) {
        NivelDificultad nivel = nivelRepository.findById(nivelId)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado: " + nivelId));
        return toResponse(nivel);
    }

    /** Últimas 50 entradas del log de auditoría (CA-05). */
    public List<AdminConfigAudit> getAuditRecientes() {
        return auditRepository.findTop50ByOrderByCreadoEnDesc();
    }

    // ─── ESCRITURA ───────────────────────────────────────────────────────────

    /**
     * Actualiza los parámetros de un nivel y registra el cambio en el log
     * de auditoría inmutable (CA-05).
     *
     * CA-03: se genera una nueva config_version. Las sesiones que ya están
     * en curso conservarán la versión anterior que se copió al inicio.
     */
    @Transactional
    public NivelConfigResponse actualizarConfiguracion(Integer nivelId,
                                                        NivelConfigRequest req,
                                                        String ipOrigen) {
        NivelDificultad nivel = nivelRepository.findById(nivelId)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado: " + nivelId));

        // Snapshot de los parámetros anteriores (para el audit)
        String parametrosAnteriores = nivel.getParametrosJson() != null
                ? nivel.getParametrosJson()
                : "{}";

        // Construir nuevo JSON con los 4 parámetros + config_version (CA-03)
        String nuevaVersion = "v" + nivelId + "_" + System.currentTimeMillis();
        Map<String, Object> nuevoJson = new LinkedHashMap<>();
        nuevoJson.put("velocidad_estimulos",  req.getVelocidadEstimulos());
        nuevoJson.put("cantidad_elementos",   req.getCantidadElementos());
        nuevoJson.put("tiempo_limite",        req.getTiempoLimite());
        nuevoJson.put("num_rondas",           req.getNumRondas());
        nuevoJson.put("config_version",       nuevaVersion);

        String parametrosNuevos;
        try {
            parametrosNuevos = objectMapper.writeValueAsString(nuevoJson);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando parámetros", e);
        }

        nivel.setParametrosJson(parametrosNuevos);
        nivelRepository.save(nivel);

        // Registrar auditoría inmutable (CA-05)
        Integer adminId = resolverAdminId();
        AdminConfigAudit audit = AdminConfigAudit.builder()
                .nivelId(nivelId)
                .juegoId(nivel.getJuego().getId())
                .adminUsuarioId(adminId)
                .parametrosAnteriores(parametrosAnteriores)
                .parametrosNuevos(parametrosNuevos)
                .ipOrigen(ipOrigen)
                .versionNueva(nuevaVersion)
                .build();
        auditRepository.save(audit);

        log.info("Config actualizada: nivelId={} version={} admin={} ip={}",
                nivelId, nuevaVersion, adminId, ipOrigen);

        return toResponse(nivel);
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    private NivelConfigResponse toResponse(NivelDificultad nivel) {
        Map<String, Object> params = parseJson(nivel.getParametrosJson());
        Juego juego = nivel.getJuego();
        return NivelConfigResponse.builder()
                .nivelId(nivel.getId())
                .juegoId(juego.getId())
                .juegoNombre(juego.getNombre())
                .nivel(nivel.getNivel())
                .configVersion((String) params.get("config_version"))
                .velocidadEstimulos(toInt(params.get("velocidad_estimulos"), 2000))
                .cantidadElementos(toInt(params.get("cantidad_elementos"),   5))
                .tiempoLimite(toInt(params.get("tiempo_limite"),             30))
                .numRondas(toInt(params.get("num_rondas"),                   10))
                .build();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String json) {
        if (json == null || json.isBlank()) return new HashMap<>();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.warn("parametros_json no parseable: {}", json);
            return new HashMap<>();
        }
    }

    private int toInt(Object val, int defaultVal) {
        if (val == null) return defaultVal;
        if (val instanceof Number n) return n.intValue();
        try { return Integer.parseInt(val.toString()); } catch (Exception e) { return defaultVal; }
    }

    /** Resuelve el ID del administrador desde el SecurityContext. */
    private Integer resolverAdminId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .map(u -> u.getId())
                .orElse(0);
    }
}
