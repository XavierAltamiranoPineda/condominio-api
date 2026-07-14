package com.condominio.condominio_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta cada request HTTP, extrae el JWT del header Authorization,
 * lo valida y establece el contexto de seguridad de Spring si el token es válido.
 *
 * <p>Sólo procesa tokens de tipo "access"; los refresh tokens son rechazados aquí.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String jwt = extractJwtFromRequest(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Rechazar refresh tokens en endpoints normales
            String tokenType = jwtTokenProvider.extractTokenType(jwt);
            if ("refresh".equals(tokenType)) {
                log.warn("Refresh token usado en endpoint protegido: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtTokenProvider.extractUsername(jwt);

            if (StringUtils.hasText(username)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtTokenProvider.isTokenValid(jwt)) {
                    java.util.List<String> roles = jwtTokenProvider.extractRoles(jwt);
                    
                    java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = new java.util.ArrayList<>(
                            roles == null ? java.util.Collections.emptyList() : 
                            roles.stream()
                                 .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                                 .toList());

                    if (roles != null && roles.contains("ROLE_ADMIN")) {
                        String[] modulos = {"ASAMBLEAS", "RESERVAS", "TICKETS", "SEGURIDAD", "RESIDENTES", "UNIDADES", "PERSONAS", "PAGOS", "CUOTAS", "MULTAS", "COMUNICADOS", "NOTIFICACIONES", "USUARIOS", "REPORTES", "CONFIGURACIONES", "CONVENIOS", "PARQUEADEROS", "VEHICULOS", "CATEGORIAS", "ADMIN"};
                        for (String m : modulos) {
                            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(m + "_LEER"));
                            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(m + "_CREAR"));
                            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(m + "_EDITAR"));
                            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(m + "_ELIMINAR"));
                            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(m));
                        }
                    }

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, // Principal is now UserDetails
                                    null, // Credentials
                                    authorities); // Roles

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Usuario autenticado desde token: {} | Roles: {}", username, authorities);
                }
            }
        } catch (Exception e) {
            log.error("No se pudo autenticar el JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el JWT del header {@code Authorization: Bearer <token>}.
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
