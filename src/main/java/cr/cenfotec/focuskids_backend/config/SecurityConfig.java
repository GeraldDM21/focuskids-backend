package cr.cenfotec.focuskids_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

// Configuracion de seguridad para permitir que el frontend de Angular se comunique con el backend
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Define que rutas estan permitidas y habilita CORS para el frontend
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Se desactiva CSRF porque la app usa tokens JWT, no cookies de sesion
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Se permite el acceso a todas las rutas de perfiles sin necesidad de login adicional
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/parent/profiles/**").permitAll()
                .anyRequest().permitAll()
            );
        return http.build();
    }

    // Configura que el frontend en localhost:4200 pueda hacer peticiones al backend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
