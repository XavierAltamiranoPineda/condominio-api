package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.TorreRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.TorreResponse;
import com.condominio.condominio_api.service.interfaces.TorreService;
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

import com.condominio.condominio_api.dto.response.UnidadResponse;
import com.condominio.condominio_api.service.interfaces.UnidadService;

@RestController
@RequestMapping("/api/v1/torres")
@RequiredArgsConstructor
@Tag(name = "Torres", description = "Gestión de torres de los condominios")
public class TorreController {

    private final TorreService torreService;
    private final UnidadService unidadService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar torres paginadas")
    public ResponseEntity<ApiResponse<Page<TorreResponse>>> listar(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(torreService.findAll(pageable), "Torres obtenidas"));
    }

    @GetMapping("/{id}/unidades")
    @PreAuthorize("hasAuthority('UNIDADES_LEER')")
    @Operation(summary = "Obtener unidades de una torre")
    public ResponseEntity<ApiResponse<Page<UnidadResponse>>> obtenerUnidades(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "numero", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(unidadService.findByTorreId(id, pageable), "Unidades obtenidas"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener torre por ID")
    public ResponseEntity<ApiResponse<TorreResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(torreService.findById(id), "Torre encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear una nueva torre")
    public ResponseEntity<ApiResponse<TorreResponse>> crear(@Valid @RequestBody TorreRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(torreService.create(request), "Torre creada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar una torre existente")
    public ResponseEntity<ApiResponse<TorreResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TorreRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(torreService.update(id, request), "Torre actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar una torre")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        torreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
