package com.condominio.condominio_api.util;

import com.condominio.condominio_api.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * Utilidad estática para construir respuestas {@link ApiResponse} con
 * {@link HttpStatus} de Spring, especialmente útil en filtros de seguridad
 * donde no se puede inyectar un bean.
 */
public final class ApiResponseUtil {

    private ApiResponseUtil() {}

    public static ApiResponse<Void> error(HttpStatus status, String message, String path) {
        return ApiResponse.<Void>builder()
                .success(false)
                .timestamp(java.time.LocalDateTime.now())
                .status(status.value())
                .message(message)
                .build();
    }

    public static ApiResponse<Void> validationError(List<Map<String, String>> errors) {
        return ApiResponse.<Void>builder()
                .success(false)
                .timestamp(java.time.LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación. Revisa los campos indicados.")
                .errors(errors)
                .build();
    }
}
