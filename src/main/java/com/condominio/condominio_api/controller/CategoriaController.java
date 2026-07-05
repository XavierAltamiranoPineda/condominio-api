package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.CategoriaRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.CategoriaResponse;
import com.condominio.condominio_api.service.interfaces.CategoriaService;
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
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de categorías para tickets u otros usos")
public class CategoriaController {

    private final CategoriaService service;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener todas las categorías")
    public ResponseEntity<ApiResponse<List<CategoriaResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(), "Categorías obtenidas"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<ApiResponse<CategoriaResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id), "Categoría encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Crear nueva categoría")
    public ResponseEntity<ApiResponse<CategoriaResponse>> create(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(service.create(request), "Categoría creada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Actualizar categoría")
    public ResponseEntity<ApiResponse<CategoriaResponse>> update(
            @PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request), "Categoría actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Eliminar categoría")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Categoría eliminada"));
    }
}
