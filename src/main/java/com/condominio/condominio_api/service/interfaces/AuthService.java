package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.LoginRequest;
import com.condominio.condominio_api.dto.request.RefreshTokenRequest;
import com.condominio.condominio_api.dto.response.AuthResponse;

/**
 * Contrato para el servicio de autenticación.
 */
public interface AuthService {

    /**
     * Autentica al usuario y retorna los tokens JWT.
     *
     * @param request credenciales de login
     * @return access token + refresh token + info del usuario
     */
    AuthResponse login(LoginRequest request);

    /**
     * Renueva el access token a partir de un refresh token válido.
     *
     * @param request refresh token
     * @return nuevo access token (el refresh token se reutiliza)
     */
    AuthResponse refreshToken(RefreshTokenRequest request);

    /**
     * Invalida la sesión del usuario (blacklist del token o registro en BD).
     *
     * @param refreshToken token a invalidar
     */
    void logout(String refreshToken);
}
