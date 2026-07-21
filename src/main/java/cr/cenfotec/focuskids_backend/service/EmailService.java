package cr.cenfotec.focuskids_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String remitente;

    public void enviarCorreoVerificacion(String destinatario, String nombre, String token) {
        String link = frontendUrl + "/auth/verify?token=" + token;

        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 480px; margin: auto;">
                    <h2 style="color:#4F46E5;">¡Hola %s!</h2>
                    <p>Gracias por registrarte en <strong>FocusKids</strong>. Para activar tu cuenta, confirma tu correo haciendo clic en el siguiente botón:</p>
                    <p style="text-align:center; margin: 24px 0;">
                        <a href="%s" style="background:#4F46E5; color:white; padding:12px 24px; border-radius:8px; text-decoration:none; font-weight:bold;">
                            Verificar mi cuenta
                        </a>
                    </p>
                    <p>Si el botón no funciona, copia y pega este enlace en tu navegador:</p>
                    <p><a href="%s">%s</a></p>
                    <p style="color:#888; font-size:12px;">Este enlace expira en 24 horas. Si no creaste esta cuenta, puedes ignorar este correo.</p>
                </div>
                """.formatted(nombre, link, link, link);

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, "UTF-8");
            helper.setTo(destinatario);
            helper.setFrom(remitente);
            helper.setSubject("Verifica tu cuenta de FocusKids");
            helper.setText(html, true);
            mailSender.send(mensaje);
            log.info("Correo de verificación enviado a {}", destinatario);
        } catch (Exception e) {
            log.error("No se pudo enviar el correo de verificación a {}: {}", destinatario, e.getMessage(), e);
            throw new RuntimeException("No se pudo enviar el correo de verificación. Intenta de nuevo más tarde.");
        }
    }
}
