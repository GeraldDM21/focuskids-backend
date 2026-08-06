package cr.cenfotec.focuskids_backend.scheduler;

import cr.cenfotec.focuskids_backend.service.ResumenSemanalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResumenSemanalScheduler {

    private final ResumenSemanalService resumenSemanalService;

    /**
     * Ejecuta el envío de resúmenes semanales cada lunes a las 8:00am (America/Costa_Rica).
     * CA-01: programación semanal.
     */
    @Scheduled(cron = "0 0 8 * * MON", zone = "America/Costa_Rica")
    public void enviarResumenesSemanal() {
        log.info("Iniciando envío de resúmenes semanales...");
        resumenSemanalService.enviarResumenes();
        log.info("Envío de resúmenes semanales completado.");
    }
}
