package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.dto.request.LoginRequest;
import com.condominio.condominio_api.dto.request.RefreshTokenRequest;
import com.condominio.condominio_api.dto.response.AuthResponse;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.security.JwtTokenProvider;
import com.condominio.condominio_api.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de autenticación.
 *
 * <p>Nota: el manejo de refresh tokens en base de datos (blacklist / rotación)
 * se implementará en la Fase 4 junto con la entidad {@code RefreshToken}.
 * Por ahora, la invalidación es stateless (el token expira solo).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Value("${app.jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    // ─── Login ────────────────────────────────────────────────────────────────

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Autenticar credenciales (lanza excepción si son inválidas)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        log.info("Login exitoso para usuario: {}", userDetails.getUsername());

        // 2. Generar tokens
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpirationMs)
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    // ─── Refresh Token ────────────────────────────────────────────────────────

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.getRefreshToken();

        // 1. Validar firma y expiración
        if (!jwtTokenProvider.isTokenValid(token)) {
            throw new org.springframework.security.authentication.BadCredentialsException("El refresh token ha expirado o es inválido.");
        }

        // 2. Verificar que es efectivamente un refresh token
        String tokenType = jwtTokenProvider.extractTokenType(token);
        if (!"refresh".equals(tokenType)) {
            throw new org.springframework.security.authentication.BadCredentialsException("El token proporcionado no es un refresh token.");
        }

        // 3. Cargar usuario y generar nuevo access token
        String username = jwtTokenProvider.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        log.info("Access token renovado para usuario: {}", username);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(token)          // El refresh token se reutiliza (sin rotación por ahora)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpirationMs)
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    // ─── Logout ───────────────────────────────────────────────────────────────

    @Override
    public void logout(String refreshToken) {
        // TODO Fase 4: guardar el token en una tabla blacklist o eliminarlo de BD
        // Por ahora, el cliente simplemente descarta el token
        log.info("Logout solicitado. Token invalidado del lado del cliente.");
    }
}
