package com.condominio.condominio_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Envoltorio estándar para TODAS las respuestas de la API.
 *
 * <pre>
 * {
 *   "success": true,
 *   "timestamp": "2025-01-01T12:00:00",
 *   "status": 200,
 *   "message": "Operación exitosa",
 *   "data": { ... },
 *   "errors": null
 * }
 * </pre>
 *
 * @param <T> tipo del campo {@code data}
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta estándar de la API")
public class ApiResponse<T> {

    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private final boolean success;

    @Schema(description = "Marca de tiempo de la respuesta")
    private final LocalDateTime timestamp;

    @Schema(description = "Código HTTP de la respuesta", example = "200")
    private final int status;

    @Schema(description = "Mensaje descriptivo", example = "Operación exitosa")
    private final String message;

    @Schema(description = "Datos retornados (null en caso de error)")
    private final T data;

    /**
     * Lista de errores de validación o detalles de fallo.
     * Cada entrada puede ser {@code Map<String,String>} con campo+mensaje.
     */
    @Schema(description = "Errores detallados (null en caso de éxito)")
    private final List<Map<String, String>> errors;

    // ─── Factories ───────────────────────────────────────────────────────────

    public static <T> ApiResponse<T> ok(T data) {
        return ok(data, "Operación exitosa");
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .status(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return created(data, "Recurso creado exitosamente");
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .status(201)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> noContent() {
        return ApiResponse.<Void>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .status(204)
                .message("Operación completada")
                .build();
    }

    public static ApiResponse<Void> error(int status, String message) {
        return ApiResponse.<Void>builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .build();
    }

    public static ApiResponse<Void> error(int status, String message, List<Map<String, String>> errors) {
        return ApiResponse.<Void>builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .errors(errors)
                .build();
    }
}
