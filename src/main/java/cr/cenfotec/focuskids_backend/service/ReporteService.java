package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final MetricaRepository metricaRepository;
    private final ResumenSemanalRepository resumenSemanalRepository;
    private final AlertaRegresionRepository alertaRegresionRepository;
    private final AnalisisTendenciaRepository analisisTendenciaRepository;
    private final RecomendacionRepository recomendacionRepository;

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
}
