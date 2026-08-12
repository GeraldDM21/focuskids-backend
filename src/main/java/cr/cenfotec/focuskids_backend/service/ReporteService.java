package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.reporte.ComparacionSesionResponse;
import cr.cenfotec.focuskids_backend.dto.reporte.HistorialSesionDTO;
import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
            LocalDateTime fechaDesde, LocalDateTime fechaHasta, int page) {

        PageRequest pageable = PageRequest.of(Math.max(0, page), PAGE_SIZE);
        Page<SesionJuego> entidades = sesionJuegoRepository.filtrarHistorial(
                perfilId, juegoId, nivel, fechaDesde, fechaHasta, pageable);

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
            LocalDateTime fechaDesde, LocalDateTime fechaHasta) {

        PerfilNino perfil = perfilNinoRepository.findById(perfilId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el perfil infantil"));

        String nombreJuegoFiltro = null;
        if (juegoId != null) {
            nombreJuegoFiltro = juegoRepository.findById(juegoId)
                    .map(Juego::getNombre)
                    .orElse(null);
        }

        List<SesionJuego> sesiones = sesionJuegoRepository.filtrarHistorialSinPaginacion(
                perfilId, juegoId, nivel, fechaDesde, fechaHasta);

        return generarPdf(perfil, nombreJuegoFiltro, nivel, fechaDesde, fechaHasta, sesiones);
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

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            documento.save(salida);
            return salida.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo generar el PDF del historial", e);
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
