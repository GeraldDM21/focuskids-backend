package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.AuthResponse;
import cr.cenfotec.focuskids_backend.dto.LoginRequest;
import cr.cenfotec.focuskids_backend.dto.RegisterRequest;
import cr.cenfotec.focuskids_backend.model.*;
import cr.cenfotec.focuskids_backend.repository.*;
import cr.cenfotec.focuskids_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PadreTutorRepository padreTutorRepository;
    private final DocenteRepository docenteRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return AuthResponse.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        usuarioRepository.save(usuario);

        // Crear perfil según el rol
        switch (request.getRol()) {
            case PADRE -> {
                PadreTutor padre = PadreTutor.builder()
                        .usuario(usuario)
                        .telefono(request.getTelefono())
                        .relacionConNino(request.getRelacionConNino())
                        .build();
                padreTutorRepository.save(padre);
            }
            case DOCENTE -> {
                Docente docente = Docente.builder()
                        .usuario(usuario)
                        .institucion(request.getInstitucion())
                        .gradoGrupo(request.getGradoGrupo())
                        .build();
                docenteRepository.save(docente);
            }
            case ADMINISTRADOR -> {
                Administrador admin = Administrador.builder()
                        .usuario(usuario)
                        .nivelAcceso(request.getNivelAcceso() != null ? request.getNivelAcceso() : "FULL")
                        .build();
                administradorRepository.save(admin);
            }
            default -> { /* NINO no tiene perfil de usuario directo */ }
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }
}
