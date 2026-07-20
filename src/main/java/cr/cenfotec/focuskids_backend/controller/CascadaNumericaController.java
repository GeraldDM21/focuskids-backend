package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.juego.*;
import cr.cenfotec.focuskids_backend.model.CascadaOperacionEvento;
import cr.cenfotec.focuskids_backend.service.CascadaNumericaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos/cascada-numerica")
@RequiredArgsConstructor
@PreAuthorize(
        "hasAnyRole('NINO', 'PADRE', 'DOCENTE', 'ADMINISTRADOR')"
)
public class CascadaNumericaController {

    private final CascadaNumericaService service;

    @PostMapping("/sesiones")
    public ResponseEntity<IniciarCascadaResponse>
    iniciarSesion(
            @Valid
            @RequestBody
            IniciarSesionRequest request
    ) {
        return ResponseEntity.ok(
                service.iniciarSesion(request)
        );
    }

    @PostMapping(
            "/sesiones/{sesionId}/operaciones"
    )
    public ResponseEntity<RegistrarOperacionResponse>
    registrarOperacion(
            @PathVariable Integer sesionId,
            @Valid
            @RequestBody
            RegistrarOperacionRequest request
    ) {
        return ResponseEntity.ok(
                service.registrarOperacion(
                        sesionId,
                        request
                )
        );
    }

    @PutMapping(
            "/sesiones/{sesionId}/finalizar"
    )
    public ResponseEntity<CascadaResultadoResponse>
    finalizarSesion(
            @PathVariable Integer sesionId,
            @Valid
            @RequestBody
            FinalizarCascadaRequest request
    ) {
        return ResponseEntity.ok(
                service.finalizarSesion(
                        sesionId,
                        request
                )
        );
    }

    @GetMapping(
            "/sesiones/{sesionId}/operaciones"
    )
    public ResponseEntity<List<CascadaOperacionEvento>>
    obtenerOperaciones(
            @PathVariable Integer sesionId
    ) {
        return ResponseEntity.ok(
                service.obtenerOperaciones(sesionId)
        );
    }
}