package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.IaRecomendacion;
import cr.cenfotec.focuskids_backend.service.IaRecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints del motor de recomendación de nivel.
 *
 * CA-04: El niño y el padre NO pueden ver ni modificar la recomendación directamente.
 *        Solo ADMINISTRADOR puede sobrescribirla.
 *        PADRE y DOCENTE pueden consultar la recomendación vigente para adaptar la experiencia.
 */
@RestController
@RequestMapping("/api/ia/recomendacion")
@RequiredArgsConstructor
public class IaRecomendacionController {

    private final IaRecomendacionService iaService;

    /**
     * Devuelve la recomendación vigente para un niño en un juego específico.
     * Usado por el frontend al cargar la pantalla de inicio de un juego.
     */
    @GetMapping("/{perfilId}/{juegoId}")
    @PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<IaRecomendacion> obtenerVigente(
            @PathVariable Integer perfilId,
            @PathVariable Integer juegoId) {

        return iaService.obtenerRecomendacion(perfilId, juegoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /**
     * Historial completo de recomendaciones para un niño + juego.
     */
    @GetMapping("/{perfilId}/{juegoId}/historial")
    @PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
    public ResponseEntity<List<IaRecomendacion>> obtenerHistorial(
            @PathVariable Integer perfilId,
            @PathVariable Integer juegoId) {

        return ResponseEntity.ok(iaService.obtenerHistorial(perfilId, juegoId));
    }

    /**
     * Dispara manualmente la generación de una nueva recomendación.
     * Normalmente se llama de forma automática al finalizar cada sesión,
     * pero este endpoint permite forzarla (útil para admin y pruebas).
     */
    @PostMapping("/{perfilId}/{juegoId}/generar")
    @PreAuthorize("hasAnyRole('PADRE', 'ADMINISTRADOR')")
    public ResponseEntity<?> generar(
            @PathVariable Integer perfilId,
            @PathVariable Integer juegoId) {

        IaRecomendacion rec = iaService.generarRecomendacion(perfilId, juegoId);
        if (rec == null) {
            return ResponseEntity.ok(Map.of(
                "mensaje", "No hay suficientes sesiones para generar una recomendación.",
                "sesionesMinimas", 3
            ));
        }
        return ResponseEntity.ok(rec);
    }

    /**
     * CA-04: Solo ADMINISTRADOR puede sobrescribir el nivel recomendado.
     * Body: { "nivelId": 5 }
     */
    @PutMapping("/{id}/sobrescribir")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<IaRecomendacion> sobrescribir(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {

        Integer nivelId = body.get("nivelId");
        if (nivelId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(iaService.sobrescribirNivel(id, nivelId));
    }
}
