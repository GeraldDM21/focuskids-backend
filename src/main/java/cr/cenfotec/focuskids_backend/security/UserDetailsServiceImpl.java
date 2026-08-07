package cr.cenfotec.focuskids_backend.security;

import cr.cenfotec.focuskids_backend.model.Usuario;
import cr.cenfotec.focuskids_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // enabled=true siempre: Spring Security solo valida contraseña aquí.
        // El control de cuenta activa/suspendida lo maneja AuthService.login()
        // (verificación de email) y JwtAuthenticationFilter (token válido).
        // Así en desarrollo se puede loguear sin verificar correo, y en
        // producción basta con descomentar la línea en AuthService.login().
        return new User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                true,            // enabled — ver comentario arriba
                true,            // accountNonExpired
                true,            // credentialsNonExpired
                true,            // accountNonLocked
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}
