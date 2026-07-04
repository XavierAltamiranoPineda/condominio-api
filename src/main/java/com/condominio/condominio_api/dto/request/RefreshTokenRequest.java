package com.condominio.condominio_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Petición para renovar el access token usando un refresh token válido.
 */
@Data
@Schema(description = "Petición para renovar el access token")
public class RefreshTokenRequest {

    @NotBlank(message = "El refresh token es obligatorio")
    @Schema(description = "Refresh token obtenido en el login", example = "eyJhbGci...")
    private String refreshToken;
}
