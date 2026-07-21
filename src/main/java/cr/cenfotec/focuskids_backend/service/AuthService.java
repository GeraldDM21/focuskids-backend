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
import java.util.Optional;
import java.util.UUID;

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
    private final EmailService emailService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // TODO [PRODUCCIÓN]: Descomentar estas líneas para hacer la verificación de correo obligatoria.
        // En desarrollo se permite el acceso sin verificar para facilitar las pruebas.
        // if (Boolean.FALSE.equals(usuario.getActivo())) {
        //     throw new RuntimeException("Debes verificar tu correo antes de iniciar sesión.");
        // }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

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
        Optional<Usuario> existente = usuarioRepository.findByEmail(request.getEmail());

        if (existente.isPresent()) {
            Usuario usuarioExistente = existente.get();

            if (Boolean.TRUE.equals(usuarioExistente.getActivo())) {
                throw new RuntimeException("Este correo ya tiene una cuenta registrada.");
            }

            // La cuenta existe pero nunca se verificó: reenviamos el correo con un token nuevo
            String nuevoToken = UUID.randomUUID().toString();
            usuarioExistente.setTokenVerificacion(nuevoToken);
            usuarioExistente.setTokenExpiracion(LocalDateTime.now().plusHours(24));
            usuarioRepository.save(usuarioExistente);

            try {
                emailService.enviarCorreoVerificacion(usuarioExistente.getEmail(), usuarioExistente.getNombre(), nuevoToken);
            } catch (Exception ignored) { /* No bloquear el registro si el correo falla */ }

            return AuthResponse.builder()
                    .usuarioId(usuarioExistente.getId())
                    .nombre(usuarioExistente.getNombre())
                    .email(usuarioExistente.getEmail())
                    .rol(usuarioExistente.getRol())
                    .mensaje("Ya tenías una cuenta pendiente de verificación con este correo. Te reenviamos el correo de verificación.")
                    .build();
        }

        String token = UUID.randomUUID().toString();

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .activo(false)
                .fechaCreacion(LocalDateTime.now())
                .tokenVerificacion(token)
                .tokenExpiracion(LocalDateTime.now().plusHours(24))
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

        try {
            emailService.enviarCorreoVerificacion(usuario.getEmail(), usuario.getNombre(), token);
        } catch (Exception ignored) { /* No bloquear el registro si el correo falla */ }

        return AuthResponse.builder()
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .mensaje("Cuenta creada. Revisa tu correo para verificarla antes de iniciar sesión.")
                .build();
    }

    @Transactional
    public String verificarCuenta(String token) {
        Usuario usuario = usuarioRepository.findByTokenVerificacion(token)
                .orElseThrow(() -> new RuntimeException(
                        "Este enlace ya fue utilizado o no es válido. Si ya verificaste tu cuenta antes, puedes iniciar sesión con normalidad."));

        if (Boolean.TRUE.equals(usuario.getActivo())) {
            return "Esta cuenta ya había sido verificada. Ya puedes iniciar sesión.";
        }

        if (usuario.getTokenExpiracion() == null || usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El enlace de verificación expiró. Solicita uno nuevo.");
        }

        usuario.setActivo(true);
        usuario.setTokenVerificacion(null);
        usuario.setTokenExpiracion(null);
        usuarioRepository.save(usuario);

        return "Cuenta verificada correctamente. Ya puedes iniciar sesión.";
    }
}

// =============================================================================
// TODO — PENDIENTES PARA PRODUCCIÓN
// =============================================================================
// [AUTH]
//   - Activar verificación obligatoria de correo en login() (ver comentario arriba).
//   - Rotar credenciales SMTP de Gmail en application.properties y moverlas
//     a variables de entorno (.env / secrets) antes de subir a producción.
//   - Agregar endpoint POST /api/auth/resend-verification para reenviar correo.
//   - Agregar endpoint POST /api/auth/forgot-password para recuperación de contraseña.
//
// [FRONTEND — REGISTRO (register.component.ts)]
//   - Rediseñar página de registro más fiel al wireframe (dos columnas, pasos).
//   - Quitar el campo "Nombre del niño/a" del registro; ese dato va en el
//     dashboard del padre al crear el primer perfil de hijo.
//   - Reemplazar emojis 🐻🦊 del selector de rol por íconos más profesionales
//     (ej. Material Icons: supervisor_account / school).
//   - Después del registro exitoso, redirigir automáticamente al login en vez de
//     mostrar el mensaje "Revisa tu correo" y esperar que el usuario haga clic.
//
// [FRONTEND — LANDING PAGE]
//   - Corregir "6 juegos cognitivos adaptativos" → "12 juegos cognitivos adaptativos".
//
// [FRONTEND — VERIFICACIÓN DE CORREO]
//   - Crear ruta /auth/verify en Angular con un VerifyComponent que lea el token
//     de la URL y llame a GET /api/auth/verify?token=... para activar la cuenta.
// =============================================================================
