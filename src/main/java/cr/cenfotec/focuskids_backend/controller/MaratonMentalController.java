package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.juego.FinalizarMaratonRequest;
import cr.cenfotec.focuskids_backend.dto.juego.IniciarMaratonRequest;
import cr.cenfotec.focuskids_backend.dto.juego.IniciarMaratonResponse;
import cr.cenfotec.focuskids_backend.dto.juego.MaratonResultadoResponse;
import cr.cenfotec.focuskids_backend.dto.juego.RegistrarRondaMaratonRequest;
import cr.cenfotec.focuskids_backend.dto.juego.RegistrarRondaMaratonResponse;
import cr.cenfotec.focuskids_backend.model.MaratonMentalRondaEvento;
import cr.cenfotec.focuskids_backend.service.MaratonMentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos/maraton-mental")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('NINO', 'PADRE', 'DOCENTE', 'ADMINISTRADOR')")
public class MaratonMentalController {

    private final MaratonMentalService service;

    @PostMapping("/sesiones")
    public ResponseEntity<IniciarMaratonResponse> iniciar(@Valid @RequestBody IniciarMaratonRequest request) {
        return ResponseEntity.ok(service.iniciarSesion(request));
    }

    @PostMapping("/sesiones/{sesionId}/rondas")
    public ResponseEntity<RegistrarRondaMaratonResponse> ronda(
            @PathVariable Integer sesionId,
            @Valid @RequestBody RegistrarRondaMaratonRequest request
    ) {
        return ResponseEntity.ok(service.registrarRonda(sesionId, request));
    }

    @PutMapping("/sesiones/{sesionId}/finalizar")
    public ResponseEntity<MaratonResultadoResponse> finalizar(
            @PathVariable Integer sesionId,
            @Valid @RequestBody FinalizarMaratonRequest request
    ) {
        return ResponseEntity.ok(service.finalizarSesion(sesionId, request));
    }

    @GetMapping("/sesiones/{sesionId}/rondas")
    public ResponseEntity<List<MaratonMentalRondaEvento>> listar(@PathVariable Integer sesionId) {
        return ResponseEntity.ok(service.obtenerRondas(sesionId));
    }
}
