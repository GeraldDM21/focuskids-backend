package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SesionService {

    private final SesionJuegoRepository sesionJuegoRepository;
    private final SessionClickEventRepository clickEventRepository;
    private final MetricaRepository metricaRepository;
    private final PerfilNinoRepository perfilNinoRepository;
    private final JuegoRepository juegoRepository;
    private final NivelDificultadRepository nivelDificultadRepository;

    @Transactional
    public SesionJuego iniciarSesion(Integer perfilId, Integer juegoId, Integer nivelId) {
        PerfilNino perfil = perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilId));
        Juego juego = juegoRepository.findById(juegoId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado: " + juegoId));
        NivelDificultad nivel = nivelDificultadRepository.findById(nivelId)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado: " + nivelId));

        SesionJuego sesion = SesionJuego.builder()
                .perfil(perfil)
                .juego(juego)
                .nivel(nivel)
                .inicio(LocalDateTime.now())
                .completada(false)
                .build();

        return sesionJuegoRepository.save(sesion);
    }

    @Transactional
    public SesionJuego finalizarSesion(Integer sesionId, Integer puntaje) {
        SesionJuego sesion = sesionJuegoRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + sesionId));
        sesion.setFin(LocalDateTime.now());
        sesion.setPuntaje(puntaje);
        sesion.setCompletada(true);
        return sesionJuegoRepository.save(sesion);
    }

    @Transactional
    public SessionClickEvent registrarEvento(Integer sesionId, SessionClickEvent evento) {
        SesionJuego sesion = sesionJuegoRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + sesionId));
        evento.setSesion(sesion);
        return clickEventRepository.save(evento);
    }

    public List<SesionJuego> obtenerPorPerfil(Integer perfilId) {
        return sesionJuegoRepository.findByPerfilIdOrderByInicioDesc(perfilId);
    }

    public List<SessionClickEvent> obtenerEventos(Integer sesionId) {
        return clickEventRepository.findBySesionId(sesionId);
    }
}
