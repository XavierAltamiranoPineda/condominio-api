package com.condominio.condominio_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Respuesta del endpoint de login y refresh token.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta de autenticación con tokens JWT")
public class AuthResponse {

    @Schema(description = "Access token JWT (vida corta, 15 min)", example = "eyJhbGci...")
    private final String accessToken;

    @Schema(description = "Refresh token JWT (vida larga, 7 días)", example = "eyJhbGci...")
    private final String refreshToken;

    @Schema(description = "Tipo de token", example = "Bearer")
    private final String tokenType;

    @Schema(description = "Tiempo de expiración del access token en ms", example = "900000")
    private final long expiresIn;

    @Schema(description = "Nombre de usuario autenticado", example = "jperez")
    private final String username;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private final String fullName;

    @Schema(description = "Roles asignados al usuario", example = "[\"ROLE_ADMIN\"]")
    private final List<String> roles;
}
