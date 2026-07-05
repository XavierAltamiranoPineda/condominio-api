package com.condominio.condominio_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuración central de Spring Security.
 *
 * <ul>
 *   <li>Stateless (JWT, sin sesiones)</li>
 *   <li>CSRF deshabilitado (API REST)</li>
 *   <li>CORS configurado desde {@link com.condominio.condominio_api.config.CorsConfig}</li>
 *   <li>{@code @EnableMethodSecurity} para {@code @PreAuthorize} en controllers/services</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Rutas públicas (no requieren autenticación)
    private static final String[] PUBLIC_PATHS = {
            // Auth
            "/api/v1/auth/**",
            // Swagger / OpenAPI
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            // Health
            "/actuator/health",
            // Error
            "/error"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ── CSRF deshabilitado (API REST sin sesiones) ──────────────
                .csrf(AbstractHttpConfigurer::disable)

                // ── CORS ────────────────────────────────────────────────────
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // ── Sesión stateless ────────────────────────────────────────
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ── Manejo de errores 401 / 403 ─────────────────────────────
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))

                // ── Reglas de autorización ──────────────────────────────────
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .anyRequest().authenticated())

                // ── Proveedor de autenticación ──────────────────────────────
                .authenticationProvider(authenticationProvider())

                // ── Filtro JWT (antes del filtro de usuario/contraseña) ─────
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
