package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.*;
import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class ProgresoService {

    private final PerfilNinoRepository        perfilRepo;
    private final SesionJuegoRepository       sesionRepo;
    private final AnalisisTendenciaRepository tendenciaRepo;

    public ProgresoDashboardResponse getProgreso(Integer perfilId) {
        PerfilNino perfil = perfilRepo.findById(perfilId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilId));

        // ── Ventana de 4 semanas ──────────────────────────────────────────
        LocalDateTime hace4Semanas = LocalDateTime.now().minusWeeks(4);
        List<SesionJuego> sesiones = sesionRepo
                .findByPerfilIdAndInicioAfterOrderByInicioDesc(perfilId, hace4Semanas);

        // ── CA-01: última sesión ──────────────────────────────────────────
        LocalDateTime ultimaSesion = sesiones.isEmpty() ? null : sesiones.get(0).getInicio();

        // ── CA-01: juego más jugado esta semana ──────────────────────────
        LocalDateTime lunesDtSemana = LocalDateTime.now()
                .with(DayOfWeek.MONDAY).with(LocalTime.MIDNIGHT);
        String juegoMasJugado = sesiones.stream()
                .filter(s -> s.getInicio().isAfter(lunesDtSemana))
                .collect(Collectors.groupingBy(s -> s.getJuego().getNombre(), Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);

        // ── CA-01: nivel actual + sesiones por juego ──────────────────────
        Map<String, String>  nivelMap    = new LinkedHashMap<>();
        Map<String, Integer> sesionesMap = new LinkedHashMap<>();
        for (SesionJuego s : sesiones) {
            String jn = s.getJuego().getNombre();
            nivelMap.putIfAbsent(jn, s.getNivel().getNivel());
            sesionesMap.merge(jn, 1, Integer::sum);
        }
        int maxSes = sesionesMap.values().stream().max(Integer::compareTo).orElse(1);
        List<NivelPorJuegoDTO> niveles = nivelMap.entrySet().stream()
                .map(e -> {
                    int cnt = sesionesMap.getOrDefault(e.getKey(), 0);
                    int pct = maxSes > 0 ? (int) Math.round(cnt * 100.0 / maxSes) : 0;
                    return new NivelPorJuegoDTO(e.getKey(), e.getValue(), cnt, pct);
                })
                .collect(Collectors.toList());

        // ── CA-01: tendencia IA ───────────────────────────────────────────
        String tendencia = calcularTendencia(perfilId);

        // ── CA-04: actividad Lun–Dom de la semana actual ──────────────────
        LocalDate hoy         = LocalDate.now();
        LocalDate lunesSemana = hoy.with(DayOfWeek.MONDAY);
        Set<LocalDate> diasSesion = sesiones.stream()
                .map(s -> s.getInicio().toLocalDate()).collect(Collectors.toSet());
        List<Boolean> actividadSemanal = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            actividadSemanal.add(diasSesion.contains(lunesSemana.plusDays(i)));

        // ── Stats de resumen ──────────────────────────────────────────────
        int totalSesiones = sesiones.size();

        int tiempoTotalMin = sesiones.stream()
                .filter(s -> s.getDuracionSesionSegundos() != null)
                .mapToInt(SesionJuego::getDuracionSesionSegundos)
                .sum() / 60;

        int precisionMedia = (int) sesiones.stream()
                .filter(s -> s.getPorcentajeAciertos() != null)
                .mapToDouble(s -> s.getPorcentajeAciertos().doubleValue())
                .average().orElse(0);

        int rachaActual = calcularRacha(diasSesion, hoy);

        // ── Minutos por día de la semana actual (Lun–Dom) ─────────────────
        List<Integer> minutosSemanales = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate dia = lunesSemana.plusDays(i);
            int minutos = sesiones.stream()
                    .filter(s -> s.getInicio().toLocalDate().equals(dia)
                              && s.getDuracionSesionSegundos() != null)
                    .mapToInt(SesionJuego::getDuracionSesionSegundos)
                    .sum() / 60;
            minutosSemanales.add(minutos);
        }

        // ── Últimas 3 actividades ─────────────────────────────────────────
        List<UltimaActividadDTO> ultimas = sesiones.stream()
                .limit(3)
                .map(s -> new UltimaActividadDTO(
                        s.getJuego().getNombre(),
                        s.getNivel().getNivel(),
                        s.getInicio(),
                        s.getPuntaje(),
                        s.getCompletada()))
                .collect(Collectors.toList());

        return ProgresoDashboardResponse.builder()
                .perfilId(perfil.getId()).nombre(perfil.getNombre())
                .avatar(perfil.getAvatar()).edad(perfil.getEdad())
                .ultimaSesion(ultimaSesion).juegoMasJugadoSemana(juegoMasJugado)
                .nivelesPorJuego(niveles).tendencia(tendencia)
                .actividadSemanal(actividadSemanal)
                .totalSesiones(totalSesiones).tiempoTotalMinutos(tiempoTotalMin)
                .precisionMedia(precisionMedia).rachaActual(rachaActual)
                .minutosSemanales(minutosSemanales).ultimasActividades(ultimas)
                .build();
    }

    private String calcularTendencia(Integer perfilId) {
        var lista = tendenciaRepo.findByPerfilIdOrderByFechaDesc(perfilId);
        if (lista.isEmpty()) return "SIN_DATOS";
        BigDecimal score = lista.get(0).getScoreTendencia();
        if (score == null) return "SIN_DATOS";
        if (score.compareTo(new BigDecimal("0.1"))  > 0) return "SUBIENDO";
        if (score.compareTo(new BigDecimal("-0.1")) < 0) return "BAJANDO";
        return "ESTABLE";
    }

    /** Días consecutivos jugados contando hacia atrás desde hoy. */
    private int calcularRacha(Set<LocalDate> diasConSesion, LocalDate hoy) {
        int racha = 0;
        LocalDate dia = hoy;
        while (diasConSesion.contains(dia)) { racha++; dia = dia.minusDays(1); }
        return racha;
    }

    public List<ProgresoDashboardResponse> getProgresoLista(List<PerfilNino> perfiles) {
        return perfiles.stream().map(p -> getProgreso(p.getId())).collect(Collectors.toList());
    }
}
