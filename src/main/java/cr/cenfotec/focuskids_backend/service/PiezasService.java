package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.PiezasSesionRequest;
import cr.cenfotec.focuskids_backend.model.PiezasSesion;
import cr.cenfotec.focuskids_backend.repository.PiezasSesionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PiezasService {

    private final PiezasSesionRepository repository;

    public PiezasSesion guardarSesion(PiezasSesionRequest req) {
        PiezasSesion sesion = PiezasSesion.builder()
                .perfilId(req.getPerfilId())
                .nivel(req.getNivel())
                .piezasTotales(req.getPiezasTotales())
                .piezasColocadas(req.getPiezasColocadas())
                .rotaciones(req.getRotaciones())
                .piezasFallidas(req.getPiezasFallidas())
                .tiempoUsadoSegundos(req.getTiempoUsadoSegundos())
                .tiempoTotalSegundos(req.getTiempoTotalSegundos())
                .puntosBonus(req.getPuntosBonus())
                .completada(req.getCompletada())
                .subioNivel(req.getSubioNivel())
                .build();
        return repository.save(sesion);
    }

    public List<PiezasSesion> getSesiones(Integer perfilId) {
        return repository.findByPerfilIdOrderByCreadoEnDesc(perfilId);
    }
}
