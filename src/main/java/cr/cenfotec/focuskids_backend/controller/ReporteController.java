package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PADRE', 'DOCENTE', 'ADMINISTRADOR')")
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/perfil/{perfilId}/metricas")
    public ResponseEntity<List<Metrica>> obtenerMetricas(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(reporteService.obtenerMetricasPorPerfil(perfilId));
    }

    @GetMapping("/perfil/{perfilId}/semanal")
    public ResponseEntity<List<ResumenSemanal>> obtenerResumenes(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(reporteService.obtenerResumenesSemanal(perfilId));
    }

    @GetMapping("/perfil/{perfilId}/alertas")
    public ResponseEntity<List<AlertaRegresion>> obtenerAlertas(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(reporteService.obtenerAlertas(perfilId));
    }

    @GetMapping("/perfil/{perfilId}/alertas/pendientes")
    public ResponseEntity<List<AlertaRegresion>> obtenerAlertasPendientes(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(reporteService.obtenerAlertasNoVistas(perfilId));
    }

    @GetMapping("/perfil/{perfilId}/tendencias")
    public ResponseEntity<List<AnalisisTendencia>> obtenerTendencias(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(reporteService.obtenerAnalisis(perfilId));
    }

    @GetMapping("/perfil/{perfilId}/recomendaciones")
    public ResponseEntity<List<Recomendacion>> obtenerRecomendaciones(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(reporteService.obtenerRecomendaciones(perfilId));
    }
}
