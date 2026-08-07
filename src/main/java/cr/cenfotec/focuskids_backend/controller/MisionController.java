package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.MisionReclamada;
import cr.cenfotec.focuskids_backend.service.MisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/misiones")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MisionController {

    private final MisionService misionService;

    /** GET /api/misiones/estado/{perfilId}
     *  Returns { reclamado: boolean, recompensa?: string, misionIndex?: number }
     */
    @GetMapping("/estado/{perfilId}")
    public ResponseEntity<Map<String, Object>> getEstado(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(misionService.getEstado(perfilId));
    }

    /** POST /api/misiones/reclamar/{perfilId}
     *  Body: { misionIndex: number, recompensa: string }
     *  Returns the MisionReclamada record (created or existing).
     */
    @PostMapping("/reclamar/{perfilId}")
    public ResponseEntity<MisionReclamada> reclamar(
            @PathVariable Integer perfilId,
            @RequestBody Map<String, Object> body) {

        Integer misionIndex = (Integer) body.get("misionIndex");
        String  recompensa  = (String)  body.get("recompensa");

        return ResponseEntity.ok(misionService.reclamar(perfilId, misionIndex, recompensa));
    }

    /** GET /api/misiones/historial/{perfilId}
     *  Returns list of all claimed missions, newest first.
     */
    @GetMapping("/historial/{perfilId}")
    public ResponseEntity<List<MisionReclamada>> getHistorial(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(misionService.getHistorial(perfilId));
    }
}
