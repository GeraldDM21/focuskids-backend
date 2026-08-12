package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.Juego;
import cr.cenfotec.focuskids_backend.model.Notificacion;
import cr.cenfotec.focuskids_backend.model.PerfilNino;
import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.NotificacionRepository;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Notificacion> obtenerPorUsuario(Integer usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public List<Notificacion> obtenerNoLeidas(Integer usuarioId) {
        return notificacionRepository.findByUsuarioIdAndLeida(usuarioId, false);
    }

    @Transactional
    public Notificacion crear(Integer usuarioId, String tipo, String mensaje) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        Notificacion notificacion = Notificacion.builder()
                .usuario(usuario)
                .tipo(tipo)
                .mensaje(mensaje)
                .leida(false)
                .fecha(LocalDateTime.now())
                .build();

        return notificacionRepository.save(notificacion);
    }

    /**
     * CA-02/CA-03: variante usada por las alertas de regresión cognitiva,
     * que además del mensaje llevan el niño, el juego y las sesiones a
     * resaltar en el historial (para el botón "Ver detalle").
     */
    @Transactional
    public Notificacion crearAlertaRegresion(Integer usuarioId, String tipo, String mensaje,
                                             PerfilNino ninoPerfil, Juego juego, String sesionesResaltadas) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        Notificacion notificacion = Notificacion.builder()
                .usuario(usuario)
                .tipo(tipo)
                .mensaje(mensaje)
                .ninoPerfil(ninoPerfil)
                .juego(juego)
                .sesionesResaltadas(sesionesResaltadas)
                .leida(false)
                .fecha(LocalDateTime.now())
                .build();

        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public Notificacion marcarLeida(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada: " + id));
        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public void marcarTodasLeidas(Integer usuarioId) {
        List<Notificacion> pendientes = notificacionRepository.findByUsuarioIdAndLeida(usuarioId, false);
        pendientes.forEach(n -> n.setLeida(true));
        notificacionRepository.saveAll(pendientes);
    }
}