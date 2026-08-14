package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.reporte.ComparacionSesionResponse;
import cr.cenfotec.focuskids_backend.dto.reporte.HistorialSesionDTO;
import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    // ── RF-Historial: Historial detallado de sesiones por juego ────────────

    /** CA-01/CA-02/CA-03: lista paginada (10 por página) con filtros combinables. */
    @GetMapping("/perfil/{perfilId}/historial")
    public ResponseEntity<Page<HistorialSesionDTO>> obtenerHistorial(
            @PathVariable Integer perfilId,
            @RequestParam(required = false) Integer juegoId,
            @RequestParam(required = false) String nivel,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            @RequestParam(required = false) Boolean soloCompletadas,
            @RequestParam(defaultValue = "0") int page) {

        return ResponseEntity.ok(reporteService.obtenerHistorialSesiones(
                perfilId, juegoId,
                (nivel != null && nivel.isBlank()) ? null : nivel,
                fechaDesde, fechaHasta, soloCompletadas, page));
    }

    /** CA-04: al expandir una sesión, comparación contra la sesión anterior del mismo juego. */
    @GetMapping("/perfil/{perfilId}/historial/{sesionId}/comparacion")
    public ResponseEntity<ComparacionSesionResponse> obtenerComparacion(
            @PathVariable Integer perfilId,
            @PathVariable Integer sesionId) {
        return ResponseEntity.ok(reporteService.obtenerComparacion(perfilId, sesionId));
    }

    /** Exporta a Excel el historial con los mismos filtros actualmente aplicados (sin paginar). */
    @GetMapping("/perfil/{perfilId}/historial/exportar-excel")
    public ResponseEntity<byte[]> exportarHistorialExcel(
            @PathVariable Integer perfilId,
            @RequestParam(required = false) Integer juegoId,
            @RequestParam(required = false) String nivel,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            @RequestParam(required = false) Boolean soloCompletadas) {

        byte[] excel = reporteService.exportarHistorialExcel(
                perfilId, juegoId,
                (nivel != null && nivel.isBlank()) ? null : nivel,
                fechaDesde, fechaHasta, soloCompletadas);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"historial-sesiones.xlsx\"")
                .body(excel);
    }

    /** CA-05: exporta a PDF el historial con los mismos filtros actualmente aplicados (sin paginar). */
    @GetMapping("/perfil/{perfilId}/historial/exportar-pdf")
    public ResponseEntity<byte[]> exportarHistorialPdf(
            @PathVariable Integer perfilId,
            @RequestParam(required = false) Integer juegoId,
            @RequestParam(required = false) String nivel,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            @RequestParam(required = false) Boolean soloCompletadas) {

        byte[] pdf = reporteService.exportarHistorialPdf(
                perfilId, juegoId,
                (nivel != null && nivel.isBlank()) ? null : nivel,
                fechaDesde, fechaHasta, soloCompletadas);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"historial-sesiones.pdf\"")
                .body(pdf);
    }
}
