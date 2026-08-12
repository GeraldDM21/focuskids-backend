package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.IaEvaluacionSesion;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class GraficoAlertaService {

    private static final int ANCHO = 640;
    private static final int ALTO = 360;
    private static final int MARGEN_IZQ = 70;
    private static final int MARGEN_DER = 40;
    private static final int MARGEN_SUP = 60;
    private static final int MARGEN_INF = 70;
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd/MM");

    private static final Color COLOR_FONDO = Color.WHITE;
    private static final Color COLOR_BARRA = new Color(0xEF, 0x44, 0x44);   // rojo (regresión)
    private static final Color COLOR_UMBRAL = new Color(0x94, 0xA3, 0xB8);  // gris
    private static final Color COLOR_EJE = new Color(0x33, 0x41, 0x55);
    private static final Color COLOR_TEXTO = new Color(0x1E, 0x29, 0x3B);

    /**
     * @param evaluaciones las 3 evaluaciones consecutivas que dispararon la
     *                     alerta, en orden cronológico
     * @param nombreJuego  nombre del juego, usado en el título del gráfico.
     */
    public byte[] generarGraficoRegresion(List<IaEvaluacionSesion> evaluaciones, String nombreJuego) {
        BufferedImage imagen = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Fondo
            g.setColor(COLOR_FONDO);
            g.fillRect(0, 0, ANCHO, ALTO);

            // Título
            g.setColor(COLOR_TEXTO);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString("Tendencia de regresión — " + nombreJuego, MARGEN_IZQ, 28);
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.setColor(COLOR_UMBRAL);
            g.drawString("Confianza del análisis en las últimas 3 evaluaciones (umbral de alerta: 0.60)", MARGEN_IZQ, 46);

            int areaAlto = ALTO - MARGEN_SUP - MARGEN_INF;
            int areaAncho = ANCHO - MARGEN_IZQ - MARGEN_DER;
            int base = ALTO - MARGEN_INF;

            g.setColor(COLOR_EJE);
            g.setStroke(new BasicStroke(1.5f));
            g.draw(new Line2D.Double(MARGEN_IZQ, MARGEN_SUP, MARGEN_IZQ, base));
            g.setFont(new Font("SansSerif", Font.PLAIN, 11));
            for (int i = 0; i <= 5; i++) {
                double valor = i / 5.0;
                int y = base - (int) (valor * areaAlto);
                g.setColor(new Color(0xE2, 0xE8, 0xF0));
                g.drawLine(MARGEN_IZQ, y, ANCHO - MARGEN_DER, y);
                g.setColor(COLOR_EJE);
                g.drawString(String.format("%.1f", valor), MARGEN_IZQ - 30, y + 4);
            }

            int yUmbral = base - (int) (0.6 * areaAlto);
            g.setColor(COLOR_UMBRAL);
            float[] guiones = {6f, 4f};
            g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, guiones, 0f));
            g.draw(new Line2D.Double(MARGEN_IZQ, yUmbral, ANCHO - MARGEN_DER, yUmbral));

            int n = evaluaciones.size();
            int anchoBarra = Math.min(90, areaAncho / (n * 2));
            int espacio = areaAncho / n;

            g.setStroke(new BasicStroke(1f));
            for (int i = 0; i < n; i++) {
                IaEvaluacionSesion evaluacion = evaluaciones.get(i);
                double confianza = evaluacion.getConfianza() != null ? evaluacion.getConfianza().doubleValue() : 0.0;
                int altoBarra = (int) (confianza * areaAlto);
                int x = MARGEN_IZQ + espacio * i + (espacio - anchoBarra) / 2;
                int y = base - altoBarra;

                g.setColor(COLOR_BARRA);
                g.fillRoundRect(x, y, anchoBarra, altoBarra, 6, 6);

                g.setColor(COLOR_TEXTO);
                g.setFont(new Font("SansSerif", Font.BOLD, 12));
                String etiquetaValor = String.format("%.2f", confianza);
                int anchoTexto = g.getFontMetrics().stringWidth(etiquetaValor);
                g.drawString(etiquetaValor, x + (anchoBarra - anchoTexto) / 2, y - 8);

                g.setFont(new Font("SansSerif", Font.PLAIN, 11));
                String etiquetaFecha = evaluacion.getFechaEvaluacion() != null
                        ? evaluacion.getFechaEvaluacion().format(FMT_FECHA)
                        : "";
                int anchoFecha = g.getFontMetrics().stringWidth(etiquetaFecha);
                g.drawString(etiquetaFecha, x + (anchoBarra - anchoFecha) / 2, base + 18);
            }

            g.setColor(COLOR_EJE);
            g.setStroke(new BasicStroke(1.5f));
            g.draw(new Line2D.Double(MARGEN_IZQ, base, ANCHO - MARGEN_DER, base));

            g.dispose();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", buffer);
            return buffer.toByteArray();
        } catch (IOException e) {
            g.dispose();
            throw new UncheckedIOException("No se pudo generar la gráfica de la alerta", e);
        }
    }
}