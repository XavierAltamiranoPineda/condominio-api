package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.ConfiguracionRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.ConfiguracionResponse;
import com.condominio.condominio_api.service.interfaces.ConfiguracionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/configuraciones")
@RequiredArgsConstructor
@Tag(name = "Configuraciones", description = "Gestión de configuraciones globales del sistema")
public class ConfiguracionController {

    private final ConfiguracionService service;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Obtener todas las configuraciones")
    public ResponseEntity<ApiResponse<List<ConfiguracionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(), "Configuraciones obtenidas"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Obtener configuración por ID")
    public ResponseEntity<ApiResponse<ConfiguracionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id), "Configuración encontrada"));
    }

    @GetMapping("/clave/{clave}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('RESIDENTE')")
    @Operation(summary = "Obtener configuración por clave")
    public ResponseEntity<ApiResponse<ConfiguracionResponse>> getByClave(@PathVariable String clave) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByClave(clave), "Configuración encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Crear nueva configuración")
    public ResponseEntity<ApiResponse<ConfiguracionResponse>> create(@Valid @RequestBody ConfiguracionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(service.create(request), "Configuración creada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Actualizar configuración")
    public ResponseEntity<ApiResponse<ConfiguracionResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ConfiguracionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request), "Configuración actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Eliminar configuración")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Configuración eliminada"));
    }
}
