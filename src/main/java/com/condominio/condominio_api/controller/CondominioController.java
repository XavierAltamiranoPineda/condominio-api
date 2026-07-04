package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.CondominioRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.CondominioResponse;
import com.condominio.condominio_api.service.interfaces.CondominioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/condominios")
@RequiredArgsConstructor
@Tag(name = "Condominios", description = "Gestión de condominios")
public class CondominioController {

    private final CondominioService condominioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar condominios paginados")
    public ResponseEntity<ApiResponse<Page<CondominioResponse>>> listar(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(condominioService.findAll(pageable), "Condominios obtenidos"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener condominio por ID")
    public ResponseEntity<ApiResponse<CondominioResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(condominioService.findById(id), "Condominio encontrado"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo condominio")
    public ResponseEntity<ApiResponse<CondominioResponse>> crear(@Valid @RequestBody CondominioRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(condominioService.create(request), "Condominio creado exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un condominio existente")
    public ResponseEntity<ApiResponse<CondominioResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CondominioRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(condominioService.update(id, request), "Condominio actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un condominio")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        condominioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
