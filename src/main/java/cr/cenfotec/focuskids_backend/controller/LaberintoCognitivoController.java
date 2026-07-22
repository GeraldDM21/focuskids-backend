package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.juego.*;
import cr.cenfotec.focuskids_backend.model.LaberintoPasoEvento;
import cr.cenfotec.focuskids_backend.service.LaberintoCognitivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// RF-29: Laberinto Cognitivo
@RestController
@RequestMapping("/api/juegos/laberinto-cognitivo")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('NINO', 'PADRE', 'DOCENTE', 'ADMINISTRADOR')")
public class LaberintoCognitivoController {

    private final LaberintoCognitivoService service;

    @PostMapping("/sesiones")
    public ResponseEntity<IniciarLaberintoResponse> iniciarSesion(
            @Valid @RequestBody IniciarLaberintoRequest request
    ) {
        return ResponseEntity.ok(service.iniciarSesion(request));
    }

    @PostMapping("/sesiones/{sesionId}/pasos")
    public ResponseEntity<RegistrarPasoResponse> registrarPaso(
            @PathVariable Integer sesionId,
            @Valid @RequestBody RegistrarPasoRequest request
    ) {
        return ResponseEntity.ok(service.registrarPaso(sesionId, request));
    }

    @PutMapping("/sesiones/{sesionId}/finalizar")
    public ResponseEntity<LaberintoResultadoResponse> finalizarSesion(
            @PathVariable Integer sesionId,
            @Valid @RequestBody FinalizarLaberintoRequest request
    ) {
        return ResponseEntity.ok(service.finalizarSesion(sesionId, request));
    }

    @GetMapping("/sesiones/{sesionId}/pasos")
    public ResponseEntity<List<LaberintoPasoEvento>> obtenerPasos(
            @PathVariable Integer sesionId
    ) {
        return ResponseEntity.ok(service.obtenerPasos(sesionId));
    }
}
