package cr.cenfotec.focuskids_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Respuesta del dashboard de progreso por perfil de niño.
 *
 * CA-01: nombre, avatar, ultimaSesion, juegoMasJugadoSemana, nivelesPorJuego, tendencia
 * CA-02: datos de las últimas 4 semanas
 * CA-04: actividadSemanal — 7 booleanos Lun(0)–Dom(6) de la semana actual
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgresoDashboardResponse {

    // ── Identificación del perfil ─────────────────────────────────────────
    private Integer perfilId;
    private String  nombre;
    private String  avatar;
    private Integer edad;

    // ── CA-01: última sesión ──────────────────────────────────────────────
    private LocalDateTime ultimaSesion;

    // ── CA-01: juego más jugado esta semana ───────────────────────────────
    private String juegoMasJugadoSemana;

    // ── CA-01: nivel actual por juego (basado en sesión más reciente) ─────
    private List<NivelPorJuegoDTO> nivelesPorJuego;

    // ── CA-01: tendencia general según IA ────────────────────────────────
    // Valores: "SUBIENDO", "ESTABLE", "BAJANDO", "SIN_DATOS"
    private String tendencia;

    // ── CA-04: actividad Lun–Dom de la semana actual ─────────────────────
    private List<Boolean> actividadSemanal;

    // ── Stats de resumen (4 semanas) ──────────────────────────────────────
    private Integer totalSesiones;       // total sesiones en 4 semanas
    private Integer tiempoTotalMinutos;  // suma de duracionSesionSegundos / 60
    private Integer precisionMedia;      // promedio porcentajeAciertos (0–100)
    private Integer rachaActual;         // días consecutivos jugados hasta hoy

    // ── Gráfico semanal: minutos jugados por día (Lun–Dom esta semana) ───
    private List<Integer> minutosSemanales;

    // ── Últimas 3 actividades ─────────────────────────────────────────────
    private List<UltimaActividadDTO> ultimasActividades;
}
