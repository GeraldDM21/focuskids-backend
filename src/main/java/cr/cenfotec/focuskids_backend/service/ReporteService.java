package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.reporte.ComparacionSesionResponse;
import cr.cenfotec.focuskids_backend.dto.reporte.HistorialSesionDTO;
import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.model.SessionClickEvent;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final MetricaRepository metricaRepository;
    private final ResumenSemanalRepository resumenSemanalRepository;
    private final AlertaRegresionRepository alertaRegresionRepository;
    private final AnalisisTendenciaRepository analisisTendenciaRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final SesionJuegoRepository sesionJuegoRepository;
    private final PerfilNinoRepository perfilNinoRepository;
    private final JuegoRepository juegoRepository;
    private final SessionClickEventRepository sessionClickEventRepository;

    // RF-Historial CA-03: 10 sesiones por página.
    private static final int PAGE_SIZE = 10;
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public List<Metrica> obtenerMetricasPorPerfil(Integer perfilId) {
        return metricaRepository.findBySesionPerfilId(perfilId);
    }

    public List<ResumenSemanal> obtenerResumenesSemanal(Integer perfilId) {
        return resumenSemanalRepository.findByPerfilIdOrderBySemanaInicioDesc(perfilId);
    }

    public List<AlertaRegresion> obtenerAlertas(Integer perfilId) {
        return alertaRegresionRepository.findByPerfilIdOrderByFechaDesc(perfilId);
    }

    public List<AlertaRegresion> obtenerAlertasNoVistas(Integer perfilId) {
        return alertaRegresionRepository.findByPerfilIdAndVista(perfilId, false);
    }

    public List<AnalisisTendencia> obtenerAnalisis(Integer perfilId) {
        return analisisTendenciaRepository.findByPerfilIdOrderByFechaDesc(perfilId);
    }

    public List<Recomendacion> obtenerRecomendaciones(Integer perfilId) {
        return recomendacionRepository.findByPerfilIdOrderByFechaDesc(perfilId);
    }

    // ── RF-Historial: Historial detallado de sesiones por juego ────────────

    /** CA-01/CA-02/CA-03: historial paginado con filtros combinables. */
    @Transactional(readOnly = true)
    public Page<HistorialSesionDTO> obtenerHistorialSesiones(
            Integer perfilId, Integer juegoId, String nivel,
            LocalDateTime fechaDesde, LocalDateTime fechaHasta,
            Boolean soloCompletadas, int page) {

        PageRequest pageable = PageRequest.of(Math.max(0, page), PAGE_SIZE);
        Page<SesionJuego> entidades = sesionJuegoRepository.filtrarHistorial(
                perfilId, juegoId, nivel, fechaDesde, fechaHasta, soloCompletadas, pageable);

        // Mapear a DTO dentro de la transacción para que las relaciones
        // @ManyToOne (juego, nivel) estén disponibles sin LazyInitializationException.
        List<HistorialSesionDTO> dtos = entidades.getContent().stream()
                .map(this::toHistorialDTO)
                .toList();

        return new PageImpl<>(dtos, pageable, entidades.getTotalElements());
    }

    /** Convierte SesionJuego → HistorialSesionDTO (debe llamarse dentro de una transacción activa). */
    private HistorialSesionDTO toHistorialDTO(SesionJuego s) {
        return HistorialSesionDTO.builder()
                .id(s.getId())
                .juego(s.getJuego() == null ? null : HistorialSesionDTO.JuegoInfo.builder()
                        .id(s.getJuego().getId())
                        .nombre(s.getJuego().getNombre())
                        .tipo(s.getJuego().getTipo())
                        .build())
                .nivel(s.getNivel() == null ? null : HistorialSesionDTO.NivelInfo.builder()
                        .id(s.getNivel().getId())
                        .nivel(s.getNivel().getNivel())
                        .build())
                .inicio(s.getInicio()  != null ? s.getInicio().toString()  : null)
                .fin   (s.getFin()     != null ? s.getFin().toString()     : null)
                .puntaje(s.getPuntaje())
                .completada(s.getCompletada())
                .duracionSesionSegundos(s.getDuracionSesionSegundos())
                .totalIntentos(s.getTotalIntentos())
                .totalAciertos(s.getTotalAciertos())
                .porcentajeAciertos(s.getPorcentajeAciertos())
                .tiempoRespuestaPromedioMs(s.getTiempoRespuestaPromedioMs())
                .rachaMaxAciertos(s.getRachaMaxAciertos())
                .sesionConcentracionBaja(s.getSesionConcentracionBaja())
                .sesionValida(s.getSesionValida())
                .build();
    }

    /** CA-04: racha máxima y concentración baja ya viven en la sesión misma; aquí se agrega la comparación. */
    public ComparacionSesionResponse obtenerComparacion(Integer perfilId, Integer sesionId) {
        SesionJuego actual = sesionJuegoRepository.findById(sesionId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la sesión"));

        if (actual.getPerfil() == null || !actual.getPerfil().getId().equals(perfilId)) {
            throw new IllegalArgumentException("La sesión no pertenece a este perfil");
        }

        Optional<SesionJuego> anteriorOpt = sesionJuegoRepository
                .findFirstByPerfilIdAndJuegoIdAndInicioLessThanOrderByInicioDesc(
                        perfilId, actual.getJuego().getId(), actual.getInicio());

        ComparacionSesionResponse.ComparacionSesionResponseBuilder resp = ComparacionSesionResponse.builder()
                .sesionActualId(actual.getId())
                .porcentajeAciertosActual(actual.getPorcentajeAciertos())
                .tiempoRespuestaPromedioMsActual(actual.getTiempoRespuestaPromedioMs())
                .puntajeActual(actual.getPuntaje())
                .duracionSegundosActual(actual.getDuracionSesionSegundos());

        if (anteriorOpt.isEmpty()) {
            return resp.haySesionAnterior(false).build();
        }

        SesionJuego anterior = anteriorOpt.get();
        resp.haySesionAnterior(true)
                .sesionAnteriorId(anterior.getId())
                .fechaSesionAnterior(anterior.getInicio())
                .porcentajeAciertosAnterior(anterior.getPorcentajeAciertos())
                .tiempoRespuestaPromedioMsAnterior(anterior.getTiempoRespuestaPromedioMs())
                .puntajeAnterior(anterior.getPuntaje())
                .duracionSegundosAnterior(anterior.getDuracionSesionSegundos())
                .deltaPorcentajeAciertos(restarBigDecimal(actual.getPorcentajeAciertos(), anterior.getPorcentajeAciertos()))
                .deltaTiempoRespuestaPromedioMs(restarBigDecimal(actual.getTiempoRespuestaPromedioMs(), anterior.getTiempoRespuestaPromedioMs()))
                .deltaPuntaje(restarInt(actual.getPuntaje(), anterior.getPuntaje()))
                .deltaDuracionSegundos(restarInt(actual.getDuracionSesionSegundos(), anterior.getDuracionSesionSegundos()));

        return resp.build();
    }

    private BigDecimal restarBigDecimal(BigDecimal actual, BigDecimal anterior) {
        if (actual == null || anterior == null) return null;
        return actual.subtract(anterior);
    }

    private Integer restarInt(Integer actual, Integer anterior) {
        if (actual == null || anterior == null) return null;
        return actual - anterior;
    }

    /** CA-05: PDF del historial con los filtros actualmente aplicados (sin paginar). */
    public byte[] exportarHistorialPdf(
            Integer perfilId, Integer juegoId, String nivel,
            LocalDateTime fechaDesde, LocalDateTime fechaHasta,
            Boolean soloCompletadas) {

        PerfilNino perfil = perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el perfil infantil"));

        String nombreJuegoFiltro = null;
        if (juegoId != null) {
            nombreJuegoFiltro = juegoRepository.findById(juegoId)
                    .map(Juego::getNombre)
                    .orElse(null);
        }

        List<SesionJuego> sesiones = sesionJuegoRepository.filtrarHistorialSinPaginacion(
                perfilId, juegoId, nivel, fechaDesde, fechaHasta, soloCompletadas);

        return generarPdf(perfil, nombreJuegoFiltro, nivel, fechaDesde, fechaHasta, sesiones);
    }

    /** Exporta el historial filtrado a un archivo Excel (.xlsx). */
    public byte[] exportarHistorialExcel(
            Integer perfilId, Integer juegoId, String nivel,
            LocalDateTime fechaDesde, LocalDateTime fechaHasta,
            Boolean soloCompletadas) {

        PerfilNino perfil = perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el perfil infantil"));

        List<SesionJuego> sesiones = sesionJuegoRepository.filtrarHistorialSinPaginacion(
                perfilId, juegoId, nivel, fechaDesde, fechaHasta, soloCompletadas);

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Historial de sesiones");

            // ── Estilos ──
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle dateStyle = wb.createCellStyle();
            CreationHelper ch = wb.getCreationHelper();
            dateStyle.setDataFormat(ch.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));

            // ── Encabezado ──
            String[] headers = {"Fecha inicio", "Juego", "Nivel", "% Aciertos",
                    "T. respuesta prom. (ms)", "Duración (min:seg)", "Puntaje",
                    "Racha máx.", "Conc. baja", "Estado"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ── Datos ──
            int rowNum = 1;
            for (SesionJuego s : sesiones) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getInicio() != null ? s.getInicio().format(FMT_FECHA) : "-");
                row.createCell(1).setCellValue(s.getJuego() != null ? s.getJuego().getNombre() : "-");
                row.createCell(2).setCellValue(s.getNivel() != null ? s.getNivel().getNivel() : "-");
                if (s.getPorcentajeAciertos() != null)
                    row.createCell(3).setCellValue(s.getPorcentajeAciertos().doubleValue());
                else row.createCell(3).setCellValue("-");
                if (s.getTiempoRespuestaPromedioMs() != null)
                    row.createCell(4).setCellValue(s.getTiempoRespuestaPromedioMs().doubleValue());
                else row.createCell(4).setCellValue("-");
                row.createCell(5).setCellValue(formatearDuracion(s.getDuracionSesionSegundos()));
                if (s.getPuntaje() != null) row.createCell(6).setCellValue(s.getPuntaje());
                else row.createCell(6).setCellValue("-");
                if (s.getRachaMaxAciertos() != null) row.createCell(7).setCellValue(s.getRachaMaxAciertos());
                else row.createCell(7).setCellValue("-");
                row.createCell(8).setCellValue(Boolean.TRUE.equals(s.getSesionConcentracionBaja()) ? "Sí" : "No");
                row.createCell(9).setCellValue(Boolean.TRUE.equals(s.getCompletada()) ? "Completa" : "Incompleta");
            }

            // ── Autoajuste de columnas ──
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            // ── Metadata de la hoja ──
            sheet.createFreezePane(0, 1);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo generar el Excel del historial", e);
        }
    }

    // ── Generación del PDF (PDFBox; sin librería de tablas, se dibuja a mano) ──

    private static final float MARGEN = 40f;
    private static final float ALTO_FILA = 16f;
    private static final float[] ANCHO_COL = {95f, 150f, 55f, 65f, 70f, 60f, 65f};
    private static final String[] TITULO_COL =
            {"Fecha", "Juego", "Nivel", "% Aciertos", "T. prom. (ms)", "Duración", "Estado"};

    private byte[] generarPdf(PerfilNino perfil, String nombreJuegoFiltro, String nivelFiltro,
                               LocalDateTime fechaDesde, LocalDateTime fechaHasta,
                               List<SesionJuego> sesiones) {
        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);
            PDPageContentStream cs = new PDPageContentStream(documento, pagina);
            float y = pagina.getMediaBox().getHeight() - MARGEN;

            // Título
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
            cs.newLineAtOffset(MARGEN, y);
            cs.showText(sanitizar("Historial de sesiones - " + perfil.getNombre()));
            cs.endText();
            y -= 22;

            // Subtítulo: filtros aplicados + total
            StringBuilder filtros = new StringBuilder();
            filtros.append("Juego: ").append(nombreJuegoFiltro != null ? nombreJuegoFiltro : "Todos");
            filtros.append("   Nivel: ").append(nivelFiltro != null ? nivelFiltro : "Todos");
            filtros.append("   Desde: ").append(fechaDesde != null ? fechaDesde.format(FMT_FECHA) : "-");
            filtros.append("   Hasta: ").append(fechaHasta != null ? fechaHasta.format(FMT_FECHA) : "-");
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 9);
            cs.newLineAtOffset(MARGEN, y);
            cs.showText(sanitizar(filtros.toString()));
            cs.endText();
            y -= 14;

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 9);
            cs.newLineAtOffset(MARGEN, y);
            cs.showText(sanitizar(sesiones.size() + " sesion(es) - Generado: "
                    + LocalDateTime.now().format(FMT_FECHA)));
            cs.endText();
            y -= 20;

            y = dibujarEncabezadoTabla(cs, y);

            for (SesionJuego s : sesiones) {
                if (y < MARGEN + ALTO_FILA) {
                    cs.close();
                    pagina = new PDPage(PDRectangle.A4);
                    documento.addPage(pagina);
                    cs = new PDPageContentStream(documento, pagina);
                    y = pagina.getMediaBox().getHeight() - MARGEN;
                    y = dibujarEncabezadoTabla(cs, y);
                }
                dibujarFila(cs, y, new String[]{
                        s.getInicio() != null ? s.getInicio().format(FMT_FECHA) : "-",
                        s.getJuego() != null ? s.getJuego().getNombre() : "-",
                        s.getNivel() != null ? s.getNivel().getNivel() : "-",
                        s.getPorcentajeAciertos() != null ? s.getPorcentajeAciertos() + "%" : "-",
                        s.getTiempoRespuestaPromedioMs() != null ? s.getTiempoRespuestaPromedioMs().toString() : "-",
                        formatearDuracion(s.getDuracionSesionSegundos()),
                        Boolean.TRUE.equals(s.getCompletada()) ? "Completa" : "Incompleta"
                }, false);
                y -= ALTO_FILA;
            }

            cs.close();

            // ── Páginas de heatmap (una por sesión con clicks AREA) ──
            for (SesionJuego s : sesiones) {
                List<SessionClickEvent> eventos = sessionClickEventRepository.findBySesionId(s.getId())
                        .stream()
                        .filter(e -> "AREA".equals(e.getElementoId()))
                        .toList();
                if (eventos.isEmpty()) continue;

                PDPage paginaHm = new PDPage(PDRectangle.A4);
                documento.addPage(paginaHm);
                PDPageContentStream csHm = new PDPageContentStream(documento, paginaHm);

                float pageW = paginaHm.getMediaBox().getWidth();
                float yHm = paginaHm.getMediaBox().getHeight() - MARGEN;

                // Título de la sesión
                csHm.beginText();
                csHm.setFont(PDType1Font.HELVETICA_BOLD, 11);
                csHm.newLineAtOffset(MARGEN, yHm);
                csHm.showText(sanitizar("Mapa de interaccion: "
                        + (s.getJuego() != null ? s.getJuego().getNombre() : "-")
                        + " — " + (s.getInicio() != null ? s.getInicio().format(FMT_FECHA) : "-")));
                csHm.endText();
                yHm -= 14;

                csHm.beginText();
                csHm.setFont(PDType1Font.HELVETICA, 8);
                csHm.newLineAtOffset(MARGEN, yHm);
                csHm.showText(sanitizar("Nivel: "
                        + (s.getNivel() != null ? s.getNivel().getNivel() : "-")
                        + "   Clicks registrados: " + eventos.size()));
                csHm.endText();
                yHm -= 16;

                float hmW = pageW - 2 * MARGEN;
                float hmH = 300f;
                dibujarHeatmapPdf(csHm, MARGEN, yHm, hmW, hmH, eventos);

                csHm.close();
            }

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            documento.save(salida);
            return salida.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo generar el PDF del historial", e);
        }
    }

    /**
     * Dibuja un heatmap de densidad de clicks en el espacio PDF dado.
     * Divide el área en una grilla de 20×12 celdas; colorea cada celda según
     * la densidad de clicks (verde→amarillo→naranja→rojo).
     */
    private void dibujarHeatmapPdf(PDPageContentStream cs,
                                    float x, float yTop,
                                    float width, float height,
                                    List<SessionClickEvent> eventos) throws IOException {
        final int COLS = 20;
        final int ROWS = 12;
        float cellW = width  / COLS;
        float cellH = height / ROWS;

        // Contar clicks por celda de la grilla
        int[][] grid = new int[COLS][ROWS];
        int maxCount = 0;
        for (SessionClickEvent e : eventos) {
            if (e.getClickX() == null || e.getClickY() == null) continue;
            int col = Math.min(COLS - 1, Math.max(0, e.getClickX() * COLS / 100));
            int row = Math.min(ROWS - 1, Math.max(0, e.getClickY() * ROWS / 100));
            grid[col][row]++;
            if (grid[col][row] > maxCount) maxCount = grid[col][row];
        }

        // Fondo
        cs.setNonStrokingColor(240, 253, 244);
        cs.addRect(x, yTop - height, width, height);
        cs.fill();

        // Celdas coloreadas por densidad
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                int count = grid[col][row];
                if (count == 0) continue;

                int[] rgb;
                if (maxCount <= 1) {
                    rgb = new int[]{220, 38, 38};
                } else {
                    double ratio = (double) count / maxCount;
                    if      (ratio < 0.25) rgb = new int[]{34,  197,  94};  // verde
                    else if (ratio < 0.50) rgb = new int[]{234, 179,   8};  // amarillo
                    else if (ratio < 0.75) rgb = new int[]{249, 115,  22};  // naranja
                    else                   rgb = new int[]{220,  38,  38};  // rojo
                }

                cs.setNonStrokingColor(rgb[0], rgb[1], rgb[2]);
                // PDF: eje y desde abajo; row=0 → parte superior → invertir
                float cellX = x + col * cellW;
                float cellY = yTop - height + (ROWS - 1 - row) * cellH;
                cs.addRect(cellX + 1f, cellY + 1f, cellW - 2f, cellH - 2f);
                cs.fill();
            }
        }

        // Borde exterior
        cs.setStrokingColor(209, 250, 229);
        cs.setLineWidth(0.8f);
        cs.addRect(x, yTop - height, width, height);
        cs.stroke();

        // Líneas de grilla
        cs.setStrokingColor(220, 252, 231);
        cs.setLineWidth(0.3f);
        for (int col = 1; col < COLS; col++) {
            cs.moveTo(x + col * cellW, yTop - height);
            cs.lineTo(x + col * cellW, yTop);
            cs.stroke();
        }
        for (int row = 1; row < ROWS; row++) {
            cs.moveTo(x,         yTop - row * cellH);
            cs.lineTo(x + width, yTop - row * cellH);
            cs.stroke();
        }

        // Leyenda de colores
        float leyY = yTop - height - 12f;
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 7);
        cs.setNonStrokingColor(100, 100, 100);
        cs.newLineAtOffset(x, leyY);
        cs.showText("Densidad: ");
        cs.endText();

        String[] lbls = {"Baja", "Media", "Alta", "Maxima"};
        int[][] colores = {{34,197,94},{234,179,8},{249,115,22},{220,38,38}};
        float lx = x + 52f;
        for (int i = 0; i < 4; i++) {
            cs.setNonStrokingColor(colores[i][0], colores[i][1], colores[i][2]);
            cs.addRect(lx, leyY - 2f, 8f, 8f);
            cs.fill();
            cs.setNonStrokingColor(80, 80, 80);
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 7);
            cs.newLineAtOffset(lx + 10f, leyY);
            cs.showText(sanitizar(lbls[i]));
            cs.endText();
            lx += 42f;
        }
    }

    private float dibujarEncabezadoTabla(PDPageContentStream cs, float y) throws IOException {
        dibujarFila(cs, y, TITULO_COL, true);
        return y - ALTO_FILA;
    }

    private void dibujarFila(PDPageContentStream cs, float y, String[] valores, boolean esEncabezado) throws IOException {
        float x = MARGEN;

        if (esEncabezado) {
            cs.setNonStrokingColor(230, 230, 230);
            float anchoTotal = 0;
            for (float a : ANCHO_COL) anchoTotal += a;
            cs.addRect(MARGEN, y - 12, anchoTotal, ALTO_FILA);
            cs.fill();
            cs.setNonStrokingColor(0, 0, 0);
        }

        for (int i = 0; i < valores.length; i++) {
            cs.beginText();
            cs.setFont(esEncabezado ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, 8);
            cs.newLineAtOffset(x + 3, y - 9);
            cs.showText(sanitizar(recortar(valores[i], ANCHO_COL[i])));
            cs.endText();
            x += ANCHO_COL[i];
        }
    }

    private String formatearDuracion(Integer segundos) {
        if (segundos == null) return "-";
        int min = segundos / 60;
        int seg = segundos % 60;
        return String.format("%d:%02d", min, seg);
    }

    /** Recorte simple por cantidad de caracteres (evita invadir la columna siguiente). */
    private String recortar(String texto, float anchoColumna) {
        if (texto == null) return "-";
        int maxChars = Math.max(4, (int) (anchoColumna / 4.3));
        return texto.length() > maxChars ? texto.substring(0, maxChars - 1) + "." : texto;
    }

    /** Las fuentes estándar de PDFBox (WinAnsiEncoding) no soportan cualquier carácter Unicode (p.ej. emojis). */
    private String sanitizar(String texto) {
        if (texto == null) return "";
        StringBuilder out = new StringBuilder(texto.length());
        for (char c : texto.toCharArray()) {
            out.append(c <= 0xFF ? c : '?');
        }
        return out.toString();
    }
}
