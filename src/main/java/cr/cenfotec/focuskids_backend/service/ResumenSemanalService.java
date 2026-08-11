package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.PadreTutor;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.SesionJuego;
import cr.cenfotec.focuskids_backend.repository.PadreTutorRepository;
import cr.cenfotec.focuskids_backend.repository.PerfilNinoRepository;
import cr.cenfotec.focuskids_backend.repository.SesionJuegoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumenSemanalService {

    private final PadreTutorRepository padreTutorRepository;
    private final PerfilNinoRepository perfilNinoRepository;
    private final SesionJuegoRepository sesionJuegoRepository;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    /**
     * Genera y envía el resumen semanal a todos los padres con preferencia activa.
     * Llamado cada lunes a las 8:00am (America/Costa_Rica) por el scheduler.
     */
    public void enviarResumenes() {
        LocalDateTime inicioSemana = LocalDateTime.now().minusDays(7);
        List<PadreTutor> padres = padreTutorRepository.findAll();

        for (PadreTutor padre : padres) {
            // CA-03: respetar preferencia del padre
            if (Boolean.FALSE.equals(padre.getPreferenciaResumenSemanal())) continue;

            String email = padre.getUsuario().getEmail();
            String nombrePadre = padre.getUsuario().getNombre();

            List<PerfilNino> ninos = perfilNinoRepository.findByPadreId(padre.getId());
            if (ninos.isEmpty()) continue;

            try {
                String html = construirHtml(nombrePadre, ninos, inicioSemana);
                emailService.enviarResumenSemanal(email, nombrePadre, html);
                log.info("Resumen semanal enviado a {}", email);
            } catch (Exception e) {
                log.error("Error enviando resumen semanal a {}: {}", email, e.getMessage());
            }
        }
    }

    // ── Construcción del HTML del correo ─────────────────────────────────────

    private String construirHtml(String nombrePadre, List<PerfilNino> ninos, LocalDateTime inicioSemana) {
        StringBuilder bloques = new StringBuilder();

        for (PerfilNino nino : ninos) {
            List<SesionJuego> sesiones = sesionJuegoRepository
                    .findByPerfilIdAndInicioAfter(nino.getId(), inicioSemana)
                    .stream()
                    .filter(s -> Boolean.TRUE.equals(s.getCompletada()))
                    .toList();

            bloques.append(bloqueNino(nino, sesiones));
        }

        String dashboardUrl = frontendUrl + "/padre/dashboard";

        return """
            <div style="font-family:'Segoe UI',Arial,sans-serif;max-width:600px;margin:auto;background:#F8FAFC;border-radius:16px;overflow:hidden;">
              <div style="background:linear-gradient(135deg,#4F46E5,#7C3AED);padding:32px 28px;text-align:center;">
                <h1 style="margin:0;color:white;font-size:24px;">📊 Resumen Semanal FocusKids</h1>
                <p style="margin:8px 0 0;color:#C7D2FE;font-size:14px;">%s</p>
              </div>
              <div style="padding:28px;">
                <p style="color:#1E293B;font-size:16px;">Hola <strong>%s</strong>,</p>
                <p style="color:#475569;font-size:14px;margin-bottom:24px;">
                  Aquí tienes el progreso de esta semana. ¡Cada sesión cuenta para el desarrollo de tus niños!
                </p>
                %s
                <div style="text-align:center;margin:32px 0 16px;">
                  <a href="%s" style="background:#4F46E5;color:white;padding:14px 28px;border-radius:12px;text-decoration:none;font-weight:bold;font-size:15px;">
                    📈 Ver detalles completos
                  </a>
                </div>
                <p style="color:#94A3B8;font-size:12px;text-align:center;">
                  Puedes desactivar este correo semanal desde la sección Configuración en tu perfil de FocusKids.
                </p>
              </div>
            </div>
            """.formatted(
                "Semana del " + inicioSemana.format(DateTimeFormatter.ofPattern("dd 'de' MMMM", new Locale("es"))),
                nombrePadre,
                bloques.toString(),
                dashboardUrl
        );
    }

    private String bloqueNino(PerfilNino nino, List<SesionJuego> sesiones) {
        // CA-04: sin sesiones esta semana
        if (sesiones.isEmpty()) {
            return """
                <div style="background:white;border-radius:12px;padding:20px;margin-bottom:16px;border-left:4px solid #F59E0B;">
                  <h3 style="margin:0 0 8px;color:#1E293B;">👦 %s</h3>
                  <p style="margin:0;color:#64748B;font-size:14px;">
                    %s no tuvo sesiones esta semana. Te invitamos a agendar tiempo de práctica.
                    ¡Incluso 15 minutos al día hacen una gran diferencia!
                  </p>
                </div>
                """.formatted(nino.getNombre(), nino.getNombre());
        }

        // Agrupar por juego
        Map<String, List<SesionJuego>> porJuego = sesiones.stream()
                .collect(Collectors.groupingBy(s -> s.getJuego().getNombre()));

        // CA-01: sesiones jugadas
        int totalSesiones = sesiones.size();

        // Calcular precisión promedio por juego y detectar mejora/estancamiento
        List<String> mejoras       = new ArrayList<>();
        List<String> estancamientos = new ArrayList<>();
        StringBuilder detalleJuegos = new StringBuilder();

        for (Map.Entry<String, List<SesionJuego>> entry : porJuego.entrySet()) {
            String juego = entry.getKey();
            List<SesionJuego> ss = entry.getValue();

            OptionalDouble promOpt = ss.stream()
                    .filter(s -> s.getPorcentajeAciertos() != null)
                    .mapToDouble(s -> s.getPorcentajeAciertos().doubleValue())
                    .average();

            if (promOpt.isEmpty()) continue;
            double prom = promOpt.getAsDouble();
            String nivelActual = ss.getLast().getNivel() != null ? ss.getLast().getNivel().getNivel() : "–";

            // Detectar tendencia comparando primera vs última sesión de la semana
            if (ss.size() >= 2) {
                double primera = ss.getFirst().getPorcentajeAciertos() != null
                        ? ss.getFirst().getPorcentajeAciertos().doubleValue() : 0;
                double ultima  = ss.getLast().getPorcentajeAciertos() != null
                        ? ss.getLast().getPorcentajeAciertos().doubleValue()  : 0;
                double delta   = ultima - primera;

                if (delta >= 10) {
                    mejoras.add("%s (↑%.0f%%)".formatted(juego, delta));
                } else if (delta <= -5) {
                    estancamientos.add(juego);
                }
            }

            detalleJuegos.append("""
                <div style="display:flex;justify-content:space-between;align-items:center;padding:8px 0;border-bottom:1px solid #F1F5F9;">
                  <span style="color:#334155;font-size:14px;">🎮 %s</span>
                  <span style="color:#6366F1;font-weight:700;font-size:13px;">%.0f%% · %s</span>
                </div>
                """.formatted(juego, prom, nivelActual));
        }

        // CA-02: texto en lenguaje natural
        String textoResumen = generarTextoNatural(nino.getNombre(), totalSesiones, mejoras, estancamientos);

        return """
            <div style="background:white;border-radius:12px;padding:20px;margin-bottom:16px;border-left:4px solid #4F46E5;">
              <h3 style="margin:0 0 4px;color:#1E293B;">👦 %s</h3>
              <p style="margin:0 0 12px;color:#64748B;font-size:13px;">%d sesiones completadas esta semana</p>
              <p style="margin:0 0 16px;color:#334155;font-size:14px;line-height:1.6;">%s</p>
              %s
            </div>
            """.formatted(nino.getNombre(), totalSesiones, textoResumen, detalleJuegos);
    }

    // CA-02: lenguaje natural
    private String generarTextoNatural(String nombre, int sesiones,
                                       List<String> mejoras, List<String> estancamientos) {
        StringBuilder texto = new StringBuilder();

        if (sesiones == 1) {
            texto.append("%s completó 1 sesión esta semana. ".formatted(nombre));
        } else {
            texto.append("%s completó %d sesiones esta semana. ".formatted(nombre, sesiones));
        }

        if (!mejoras.isEmpty()) {
            texto.append("%s mostró una mejora notable en %s. ¡Excelente progreso! "
                    .formatted(nombre, String.join(" y ", mejoras)));
        }

        if (!estancamientos.isEmpty()) {
            texto.append("Te recomendamos reforzar %s donde tuvo algunas dificultades. "
                    .formatted(String.join(" y ", estancamientos)));
        }

        if (mejoras.isEmpty() && estancamientos.isEmpty()) {
            texto.append("Mantuvo un desempeño consistente en todos sus juegos. ¡Sigue así!");
        }

        return texto.toString();
    }
}
