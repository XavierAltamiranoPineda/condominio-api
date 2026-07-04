package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.LoginRequest;
import com.condominio.condominio_api.dto.request.RefreshTokenRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.AuthResponse;
import com.condominio.condominio_api.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints públicos de autenticación.
 * Ruta base: {@code /api/v1/auth}
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login, renovación de token y cierre de sesión")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/v1/auth/login
     * Autentica al usuario y retorna access + refresh token.
     */
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica con usuario y contraseña. Retorna JWT access token (15 min) y refresh token (7 días)."
    )
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(authResponse, "Login exitoso"));
    }

    /**
     * POST /api/v1/auth/refresh
     * Renueva el access token usando un refresh token válido.
     */
    @PostMapping("/refresh")
    @Operation(
            summary = "Renovar access token",
            description = "Usa el refresh token para obtener un nuevo access token sin volver a ingresar credenciales."
    )
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        AuthResponse authResponse = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.ok(authResponse, "Token renovado exitosamente"));
    }

    /**
     * POST /api/v1/auth/logout
     * Invalida el refresh token (el cliente debe descartar ambos tokens).
     */
    @PostMapping("/logout")
    @Operation(
            summary = "Cerrar sesión",
            description = "Invalida el refresh token. El cliente debe eliminar ambos tokens de su almacenamiento."
    )
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken) {

        authService.logout(refreshToken);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
