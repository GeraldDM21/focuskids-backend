package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.HistoriaVivaEvaluacionRequest;
import cr.cenfotec.focuskids_backend.dto.HistoriaVivaEvaluacionResponse;
import cr.cenfotec.focuskids_backend.service.HistoriaVivaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints para Historia Viva.
 *
 * POST /api/historia-viva/evaluar-respuesta
 *   Evalúa la respuesta abierta del niño (Nivel 4) usando un LLM.
 *   Requiere autenticación (cualquier rol).
 */
@RestController
@RequestMapping("/api/historia-viva")
@RequiredArgsConstructor
public class HistoriaVivaController {

    private final HistoriaVivaService historiaVivaService;

    @PostMapping("/evaluar-respuesta")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HistoriaVivaEvaluacionResponse> evaluarRespuesta(
            @RequestBody HistoriaVivaEvaluacionRequest request) {

        HistoriaVivaEvaluacionResponse respuesta = historiaVivaService.evaluarRespuesta(request);
        return ResponseEntity.ok(respuesta);
    }
}
