package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.IaEvaluacionSesion;
import cr.cenfotec.focuskids_backend.repository.IaEvaluacionSesionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CA-05: expone los resultados del Motor de IA (análisis de tendencias).
 */
@RestController
@RequestMapping("/api/ia")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
public class IaEvaluacionController {

    private final IaEvaluacionSesionRepository iaEvaluacionSesionRepository;

    /**
     * Devuelve la evaluación de tendencia más reciente por cada nivel que el
     * niño haya jugado en el juego indicado. Si aún no hay al menos 3
     * sesiones válidas en ningún nivel (CA-01), la lista vendrá vacía.
     */
    @GetMapping("/evaluacion/{ninoId}/{juegoId}")
    public ResponseEntity<List<IaEvaluacionSesion>> obtenerEvaluacion(@PathVariable Integer ninoId,
                                                                       @PathVariable Integer juegoId) {
        List<IaEvaluacionSesion> todas = iaEvaluacionSesionRepository
                .findByNinoPerfilIdAndJuegoIdOrderByFechaEvaluacionDesc(ninoId, juegoId);

        // Nos quedamos con la más reciente por nivel (la lista ya viene
        // ordenada desc por fecha, así que el primer hit por nivel es el último).
        Map<String, IaEvaluacionSesion> masRecientePorNivel = new LinkedHashMap<>();
        for (IaEvaluacionSesion evaluacion : todas) {
            masRecientePorNivel.putIfAbsent(evaluacion.getNivel(), evaluacion);
        }

        return ResponseEntity.ok(List.copyOf(masRecientePorNivel.values()));
    }
}
