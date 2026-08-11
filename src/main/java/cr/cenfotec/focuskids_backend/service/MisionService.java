package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.MisionReclamada;
import cr.cenfotec.focuskids_backend.repository.MisionReclamadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MisionService {

    private final MisionReclamadaRepository repo;

    /**
     * Returns today's mission claim state for a profile.
     * Response map contains:
     *   reclamado  (boolean)
     *   recompensa (String | null)
     *   misionIndex (int | null)
     */
    public Map<String, Object> getEstado(Integer perfilId) {
        Optional<MisionReclamada> hoy = repo.findByPerfilIdAndFecha(perfilId, LocalDate.now());
        if (hoy.isPresent()) {
            MisionReclamada m = hoy.get();
            return Map.of(
                "reclamado",   true,
                "recompensa",  m.getRecompensa(),
                "misionIndex", m.getMisionIndex()
            );
        }
        return Map.of("reclamado", false);
    }

    /**
     * Claims today's mission reward. Idempotent – if already claimed today, returns the existing record.
     */
    public MisionReclamada reclamar(Integer perfilId, Integer misionIndex, String recompensa) {
        return repo.findByPerfilIdAndFecha(perfilId, LocalDate.now())
            .orElseGet(() -> repo.save(
                MisionReclamada.builder()
                    .perfilId(perfilId)
                    .fecha(LocalDate.now())
                    .misionIndex(misionIndex)
                    .recompensa(recompensa)
                    .build()
            ));
    }

    /**
     * Returns the full history of claimed missions for a profile.
     */
    public List<MisionReclamada> getHistorial(Integer perfilId) {
        return repo.findByPerfilIdOrderByFechaDesc(perfilId);
    }
}
