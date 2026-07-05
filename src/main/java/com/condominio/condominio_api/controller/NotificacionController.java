package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.NotificacionRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.NotificacionResponse;
import com.condominio.condominio_api.service.interfaces.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Gestión de notificaciones push, sms, email")
public class NotificacionController {

    private final NotificacionService service;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener notificación por ID")
    public ResponseEntity<ApiResponse<NotificacionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id), "Notificación encontrada"));
    }

    @GetMapping("/persona/{personaId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener notificaciones de una persona")
    public ResponseEntity<ApiResponse<Page<NotificacionResponse>>> getByPersonaId(
            @PathVariable Long personaId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByPersonaId(personaId, pageable), "Notificaciones obtenidas"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')") // Usualmente generado internamente, pero exponerlo puede servir para envíos manuales
    @Operation(summary = "Crear notificación")
    public ResponseEntity<ApiResponse<NotificacionResponse>> create(@Valid @RequestBody NotificacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(service.create(request), "Notificación creada"));
    }

    @PatchMapping("/{id}/enviar")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Marcar notificación como enviada")
    public ResponseEntity<ApiResponse<NotificacionResponse>> marcarComoEnviada(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.marcarComoEnviada(id), "Notificación marcada como enviada"));
    }

    @PatchMapping("/{id}/leer")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marcar notificación como leída")
    public ResponseEntity<ApiResponse<NotificacionResponse>> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.marcarComoLeida(id), "Notificación marcada como leída"));
    }
}
