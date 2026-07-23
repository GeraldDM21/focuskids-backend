package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.juego.FinalizarLabRequest;
import cr.cenfotec.focuskids_backend.dto.juego.IniciarLabRequest;
import cr.cenfotec.focuskids_backend.dto.juego.IniciarLabResponse;
import cr.cenfotec.focuskids_backend.dto.juego.LabResultadoResponse;
import cr.cenfotec.focuskids_backend.dto.juego.RegistrarIntentoLabRequest;
import cr.cenfotec.focuskids_backend.dto.juego.RegistrarIntentoLabResponse;
import cr.cenfotec.focuskids_backend.model.LabCienciasIntento;
import cr.cenfotec.focuskids_backend.service.LabCienciasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juegos/lab-ciencias")
@RequiredArgsConstructor
@PreAuthorize(
        "hasAnyRole('NINO', 'PADRE', 'DOCENTE', 'ADMINISTRADOR')"
)
public class LabCienciasController {

    private final LabCienciasService service;

    @PostMapping("/sesiones")
    public ResponseEntity<IniciarLabResponse> iniciar(
            @Valid
            @RequestBody
            IniciarLabRequest request
    ) {
        return ResponseEntity.ok(
                service.iniciarSesion(request)
        );
    }

    @PostMapping("/sesiones/{sesionId}/intentos")
    public ResponseEntity<RegistrarIntentoLabResponse> intento(
            @PathVariable
            Integer sesionId,

            @Valid
            @RequestBody
            RegistrarIntentoLabRequest request
    ) {
        return ResponseEntity.ok(
                service.registrarIntento(
                        sesionId,
                        request
                )
        );
    }

    @PutMapping("/sesiones/{sesionId}/finalizar")
    public ResponseEntity<LabResultadoResponse> finalizar(
            @PathVariable
            Integer sesionId,

            @Valid
            @RequestBody
            FinalizarLabRequest request
    ) {
        return ResponseEntity.ok(
                service.finalizarSesion(
                        sesionId,
                        request
                )
        );
    }

    @GetMapping("/sesiones/{sesionId}/intentos")
    public ResponseEntity<List<LabCienciasIntento>> listar(
            @PathVariable
            Integer sesionId
    ) {
        return ResponseEntity.ok(
                service.obtenerIntentos(sesionId)
        );
    }
}