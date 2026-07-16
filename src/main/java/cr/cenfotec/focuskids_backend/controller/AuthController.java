package cr.cenfotec.focuskids_backend.controller;

import cr.cenfotec.focuskids_backend.dto.AuthResponse;
import cr.cenfotec.focuskids_backend.dto.LoginRequest;
import cr.cenfotec.focuskids_backend.dto.RegisterRequest;
import cr.cenfotec.focuskids_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verify(@RequestParam String token) {
        String mensaje = authService.verificarCuenta(token);
        return ResponseEntity.ok(Map.of("mensaje", mensaje));
    }
}
