package com.condominio.condominio_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilidad para generación, validación y extracción de claims de JWT.
 *
 * <p>Maneja dos tipos de tokens:
 * <ul>
 *   <li><b>Access Token</b>: vida corta (15 min), usado en cada request.</li>
 *   <li><b>Refresh Token</b>: vida larga (7 días), usado para renovar el access token.</li>
 * </ul>
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${app.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    // ─── Generación de tokens ────────────────────────────────────────────────

    /**
     * Genera un Access Token para el usuario dado.
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("type", "access");
        extraClaims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .toList());
        return buildToken(extraClaims, userDetails, accessTokenExpirationMs);
    }

    /**
     * Genera un Refresh Token para el usuario dado.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("type", "refresh");
        return buildToken(extraClaims, userDetails, refreshTokenExpirationMs);
    }

    private String buildToken(Map<String, Object> extraClaims,
                              UserDetails userDetails,
                              long expirationMs) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    // ─── Extracción de claims ────────────────────────────────────────────────

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    @SuppressWarnings("unchecked")
    public java.util.List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof java.util.List<?>) {
                return (java.util.List<String>) rolesObj;
            }
            return java.util.Collections.emptyList();
        });
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ─── Validación ──────────────────────────────────────────────────────────

    /**
     * Valida que el token pertenece al usuario y no ha expirado.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Valida únicamente la firma y expiración (sin UserDetails).
     * Útil para el flujo de refresh token.
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT inválido: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ─── Clave de firma ──────────────────────────────────────────────────────

    private SecretKey getSigningKey() {
        // Si el secreto es texto plano (dev), lo codificamos; si es Base64, decodificamos.
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            // El secreto no es Base64 válido (por ejemplo, tiene guiones)
            // -> usarlo como raw bytes (solo dev)
            return Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
    }
}
