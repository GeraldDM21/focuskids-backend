package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.IaAlertaRepository;
import cr.cenfotec.focuskids_backend.repository.IaEvaluacionSesionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IaAlertaService {

    private static final BigDecimal CONFIANZA_MINIMA = new BigDecimal("0.6");
    private static final int SESIONES_CONSECUTIVAS = 3;
    private static final int DIAS_SIN_REENVIO = 7;

    private final IaAlertaRepository iaAlertaRepository;
    private final IaEvaluacionSesionRepository iaEvaluacionSesionRepository;
    private final EmailService emailService;
    private final NotificacionService notificacionService;
    private final GraficoAlertaService graficoAlertaService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    /**
     * Se invoca justo después de guardar una nueva {@link IaEvaluacionSesion}.
     * No propaga excepciones: cualquier fallo queda en logs para no afectar
     * el flujo del Motor de IA que la llama.
     */
    public void evaluarYAlertar(IaEvaluacionSesion evaluacionActual) {
        try {
            procesarAlerta(evaluacionActual);
        } catch (Exception ex) {
            log.error("No se pudo procesar la alerta de regresión (evaluacionId={}): {}",
                    evaluacionActual.getId(), ex.getMessage(), ex);
        }
    }

    private void procesarAlerta(IaEvaluacionSesion evaluacionActual) {
        PerfilNino perfil = evaluacionActual.getNinoPerfil();
        Juego juego = evaluacionActual.getJuego();
        String nivel = evaluacionActual.getNivel();

        // CA-01: últimas 3 evaluaciones consecutivas del mismo niño+juego+nivel.
        List<IaEvaluacionSesion> ultimasDesc = iaEvaluacionSesionRepository
                .findTop3ByNinoPerfilIdAndJuegoIdAndNivelOrderByFechaEvaluacionDesc(
                        perfil.getId(), juego.getId(), nivel);

        if (ultimasDesc.size() < SESIONES_CONSECUTIVAS) {
            return;
        }

        boolean cumpleCondicion = ultimasDesc.stream().allMatch(e ->
                e.getTendencia() == TendenciaCognitiva.REGRESION
                        && e.getConfianza() != null
                        && e.getConfianza().compareTo(CONFIANZA_MINIMA) >= 0);

        if (!cumpleCondicion) {
            return;
        }

        // CA-03: enfriamiento de 7 días por niño+juego.
        LocalDateTime desde = LocalDateTime.now().minusDays(DIAS_SIN_REENVIO);
        boolean yaEnviada = iaAlertaRepository.existsByPerfilIdAndJuegoIdAndEstadoAndFechaEnvioAfter(
                perfil.getId(), juego.getId(), EstadoAlerta.ENVIADA, desde);
        if (yaEnviada) {
            log.debug("Alerta de regresión omitida (enfriamiento de 7 días activo): perfilId={}, juegoId={}",
                    perfil.getId(), juego.getId());
            return;
        }

        List<Usuario> destinatarios = resolverDestinatarios(perfil);
        if (destinatarios.isEmpty()) {
            log.warn("Regresión detectada pero el niño (perfilId={}) no tiene padre/docente con correo registrado",
                    perfil.getId());
            return;
        }

        // Orden cronológico (más antigua primero) para la gráfica.
        List<IaEvaluacionSesion> ultimasAsc = new ArrayList<>(ultimasDesc);
        Collections.reverse(ultimasAsc);

        byte[] grafico = graficoAlertaService.generarGraficoRegresion(ultimasAsc, juego.getNombre());
        String htmlCorreo = construirHtml(perfil, juego, nivel);
        String mensajeInApp = "%s muestra señales de regresión sostenida en %s. Revisa su progreso en FocusKids."
                .formatted(perfil.getNombre(), juego.getNombre());

        // CA-03 (Notificaciones in-app): sesiones exactas a resaltar en el
        // historial cuando el destinatario haga clic en "Ver detalle".
        String sesionesResaltadas = ultimasAsc.stream()
                .map(e -> e.getSesion() != null ? String.valueOf(e.getSesion().getId()) : null)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.joining(","));

        for (Usuario destinatario : destinatarios) {
            enviarPorCorreo(perfil, juego, nivel, destinatario, htmlCorreo, grafico);
            enviarInApp(perfil, juego, nivel, destinatario, mensajeInApp, sesionesResaltadas);
        }
    }

    // CA-02: destinatarios = padre/tutor del niño y, si tiene, su docente.
    private List<Usuario> resolverDestinatarios(PerfilNino perfil) {
        List<Usuario> destinatarios = new ArrayList<>();
        if (perfil.getPadre() != null && perfil.getPadre().getUsuario() != null) {
            destinatarios.add(perfil.getPadre().getUsuario());
        }
        if (perfil.getDocente() != null && perfil.getDocente().getUsuario() != null) {
            destinatarios.add(perfil.getDocente().getUsuario());
        }
        return destinatarios;
    }

    private void enviarPorCorreo(PerfilNino perfil, Juego juego, String nivel, Usuario destinatario,
                                 String htmlCorreo, byte[] grafico) {
        IaAlerta.IaAlertaBuilder registro = IaAlerta.builder()
                .perfil(perfil)
                .juego(juego)
                .nivel(nivel)
                .destinatario(destinatario)
                .canal(CanalAlerta.EMAIL)
                .fechaEnvio(LocalDateTime.now());
        try {
            emailService.enviarAlertaRegresion(
                    destinatario.getEmail(), perfil.getNombre(), juego.getNombre(), htmlCorreo, grafico);
            iaAlertaRepository.save(registro.estado(EstadoAlerta.ENVIADA).build());
        } catch (Exception e) {
            // CA-05: se registra como fallida pero no se interrumpe el flujo
            // (aún se intenta la notificación in-app).
            iaAlertaRepository.save(registro.estado(EstadoAlerta.FALLIDA).build());
            log.error("Fallo al enviar alerta por correo a {}: {}", destinatario.getEmail(), e.getMessage());
        }
    }

    // CA-04: notificación in-app (campana), mismo contenido que el correo.
    private void enviarInApp(PerfilNino perfil, Juego juego, String nivel, Usuario destinatario,
                             String mensaje, String sesionesResaltadas) {
        IaAlerta.IaAlertaBuilder registro = IaAlerta.builder()
                .perfil(perfil)
                .juego(juego)
                .nivel(nivel)
                .destinatario(destinatario)
                .canal(CanalAlerta.INAPP)
                .fechaEnvio(LocalDateTime.now());
        try {
            notificacionService.crearAlertaRegresion(
                    destinatario.getId(), "ALERTA_REGRESION", mensaje, perfil, juego, sesionesResaltadas);
            iaAlertaRepository.save(registro.estado(EstadoAlerta.ENVIADA).build());
        } catch (Exception e) {
            iaAlertaRepository.save(registro.estado(EstadoAlerta.FALLIDA).build());
            log.error("Fallo al crear notificación in-app para usuarioId={}: {}",
                    destinatario.getId(), e.getMessage());
        }
    }

    // ── CA-02: plantilla HTML del correo (mismo estilo visual de FocusKids) ──
    private String construirHtml(PerfilNino perfil, Juego juego, String nivel) {
        String dashboardUrl = frontendUrl + "/padre/dashboard";
        return """
            <div style="font-family:'Segoe UI',Arial,sans-serif;max-width:600px;margin:auto;background:#F8FAFC;border-radius:16px;overflow:hidden;">
              <div style="background:linear-gradient(135deg,#EF4444,#F59E0B);padding:32px 28px;text-align:center;">
                <h1 style="margin:0;color:white;font-size:22px;">⚠️ Alerta de FocusKids</h1>
                <p style="margin:8px 0 0;color:#FEE2E2;font-size:14px;">Detección de regresión cognitiva</p>
              </div>
              <div style="padding:28px;">
                <p style="color:#1E293B;font-size:16px;">Hola,</p>
                <p style="color:#334155;font-size:14px;line-height:1.6;">
                  Nuestro sistema detectó que <strong>%s</strong> ha mostrado una
                  <strong>tendencia de regresión sostenida</strong> en el juego
                  <strong>%s</strong> (nivel %s) durante sus últimas 3 evaluaciones.
                </p>
                <p style="color:#334155;font-size:14px;line-height:1.6;">
                  Esto significa que su desempeño ha ido disminuyendo de forma
                  consistente en sesiones recientes. Te adjuntamos una gráfica con
                  el detalle de esas 3 evaluaciones.
                </p>
                <div style="background:#FEF2F2;border-left:4px solid #EF4444;border-radius:8px;padding:16px;margin:20px 0;">
                  <p style="margin:0;color:#7F1D1D;font-size:13px;">
                    Te recomendamos revisar su progreso y, si es posible, conversar
                    con %s sobre cómo se ha sentido durante sus sesiones de juego.
                  </p>
                </div>
                <div style="text-align:center;margin:28px 0 16px;">
                  <a href="%s" style="background:#4F46E5;color:white;padding:14px 28px;border-radius:12px;text-decoration:none;font-weight:bold;font-size:15px;">
                    📈 Ver progreso en FocusKids
                  </a>
                </div>
                <p style="color:#94A3B8;font-size:12px;text-align:center;">
                  Esta alerta no se repetirá para %s en %s durante los próximos 7 días,
                  aunque la tendencia continúe.
                </p>
              </div>
            </div>
            """.formatted(
                perfil.getNombre(), juego.getNombre(), nivel,
                perfil.getNombre(), dashboardUrl,
                perfil.getNombre(), juego.getNombre()
        );
    }
}