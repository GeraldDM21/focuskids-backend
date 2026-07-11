package cr.cenfotec.focuskids_backend;

import cr.cenfotec.focuskids_backend.controller.AdminController;
import cr.cenfotec.focuskids_backend.controller.PerfilNinoController;
import cr.cenfotec.focuskids_backend.controller.ReporteController;
import cr.cenfotec.focuskids_backend.controller.SesionController;
import cr.cenfotec.focuskids_backend.security.JwtAuthenticationFilter;
import cr.cenfotec.focuskids_backend.security.UserDetailsServiceImpl;
import cr.cenfotec.focuskids_backend.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de seguridad — CA-05
 * Verifica que los endpoints rechacen accesos sin token o con rol incorrecto.
 *
 * Usa @TestConfiguration con @EnableMethodSecurity para activar @PreAuthorize
 * sin necesitar la base de datos ni el filtro JWT real.
 */
@WebMvcTest(controllers = {
    AdminController.class,
    PerfilNinoController.class,
    SesionController.class,
    ReporteController.class
})
@Import(SecurityControllerTest.TestSecurityConfig.class)
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mocks de servicios requeridos por los controllers
    @MockitoBean private AdminService adminService;
    @MockitoBean private PerfilNinoService perfilNinoService;
    @MockitoBean private SesionService sesionService;
    @MockitoBean private ReporteService reporteService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockitoBean private UserDetailsServiceImpl userDetailsService;

    /**
     * Configuración de seguridad mínima para tests.
     * Activa @EnableMethodSecurity (para que @PreAuthorize funcione)
     * y define reglas URL-level básicas, sin el filtro JWT real.
     */
    @TestConfiguration
    @EnableMethodSecurity
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                    .requestMatchers("/api/perfil/**", "/api/sesiones/**", "/api/reportes/**")
                        .hasAnyRole("PADRE", "DOCENTE", "ADMINISTRADOR")
                    .anyRequest().authenticated()
                )
                .sessionManagement(s ->
                    s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                    // Sin token → 401 (no 403)
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );
            return http.build();
        }
    }

    // ─────────────────────────────────────────────
    // CA-02: Sin token → 401
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("CA-02: GET /api/perfil/padre/1 sin token → 401")
    void sinToken_perfilNino_retorna401() throws Exception {
        mockMvc.perform(get("/api/perfil/padre/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("CA-02: GET /api/admin/usuarios sin token → 401")
    void sinToken_admin_retorna401() throws Exception {
        mockMvc.perform(get("/api/admin/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("CA-02: GET /api/sesiones/perfil/1 sin token → 401")
    void sinToken_sesiones_retorna401() throws Exception {
        mockMvc.perform(get("/api/sesiones/perfil/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("CA-02: GET /api/reportes/perfil/1/metricas sin token → 401")
    void sinToken_reportes_retorna401() throws Exception {
        mockMvc.perform(get("/api/reportes/perfil/1/metricas"))
                .andExpect(status().isUnauthorized());
    }

    // ─────────────────────────────────────────────
    // CA-03: Token válido pero rol incorrecto → 403
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("CA-03: PADRE intenta acceder a /api/admin/usuarios → 403")
    @WithMockUser(username = "padre@test.com", roles = {"PADRE"})
    void padre_accedeAdmin_retorna403() throws Exception {
        mockMvc.perform(get("/api/admin/usuarios"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("CA-03: DOCENTE intenta acceder a /api/admin/usuarios → 403")
    @WithMockUser(username = "docente@test.com", roles = {"DOCENTE"})
    void docente_accedeAdmin_retorna403() throws Exception {
        mockMvc.perform(get("/api/admin/usuarios"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("CA-03: Usuario con rol NINO no puede acceder a /api/perfil → 403")
    @WithMockUser(username = "nino@test.com", roles = {"NINO"})
    void nino_accedePerfiles_retorna403() throws Exception {
        // PerfilNinoController tiene @PreAuthorize("hasAnyRole('PADRE','DOCENTE','ADMINISTRADOR')")
        // Un NINO no tiene ninguno de esos roles → 403
        mockMvc.perform(get("/api/perfil/padre/1"))
                .andExpect(status().isForbidden());
    }

    // ─────────────────────────────────────────────
    // CA-01: Token válido con rol correcto → 200
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("CA-01: ADMINISTRADOR accede a /api/admin/usuarios → 200")
    @WithMockUser(username = "admin@test.com", roles = {"ADMINISTRADOR"})
    void admin_accedeAdmin_retorna200() throws Exception {
        mockMvc.perform(get("/api/admin/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CA-01: PADRE accede a /api/perfil/padre/1 → 200")
    @WithMockUser(username = "padre@test.com", roles = {"PADRE"})
    void padre_accedePerfiles_retorna200() throws Exception {
        mockMvc.perform(get("/api/perfil/padre/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CA-01: DOCENTE accede a /api/reportes/perfil/1/metricas → 200")
    @WithMockUser(username = "docente@test.com", roles = {"DOCENTE"})
    void docente_accedeReportes_retorna200() throws Exception {
        mockMvc.perform(get("/api/reportes/perfil/1/metricas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CA-01: PADRE accede a /api/sesiones/perfil/1 → 200")
    @WithMockUser(username = "padre@test.com", roles = {"PADRE"})
    void padre_accedeSesiones_retorna200() throws Exception {
        mockMvc.perform(get("/api/sesiones/perfil/1"))
                .andExpect(status().isOk());
    }
}
