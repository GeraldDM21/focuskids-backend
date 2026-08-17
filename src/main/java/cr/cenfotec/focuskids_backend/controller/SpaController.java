package cr.cenfotec.focuskids_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Redirige todas las rutas del frontend (Angular) a index.html
 * para que el router de Angular maneje la navegación.
 * Las rutas /api/** siguen siendo manejadas por los RestControllers.
 */
@Controller
public class SpaController {

    @RequestMapping(value = {
        "/",
        "/auth/**",
        "/padre/**",
        "/docente/**",
        "/nino/**",
        "/admin/**",
        "/unauthorized"
    })
    public String spa() {
        return "forward:/index.html";
    }
}
