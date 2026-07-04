package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.UnidadRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.UnidadResponse;
import com.condominio.condominio_api.entity.enums.TipoUnidadEnum;
import com.condominio.condominio_api.service.interfaces.UnidadService;
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
@RequestMapping("/api/v1/unidades")
@RequiredArgsConstructor
@Tag(name = "Unidades", description = "Gestión de unidades (departamentos, casas, locales, oficinas)")
public class UnidadController {

    private final UnidadService unidadService;

    @GetMapping
    @PreAuthorize("hasAuthority('UNIDADES_LEER')")
    @Operation(summary = "Listar unidades con filtros opcionales (paginado)")
    public ResponseEntity<ApiResponse<Page<UnidadResponse>>> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) TipoUnidadEnum tipo,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        
        Page<UnidadResponse> result;
        if (estado != null) {
            result = unidadService.findByEstado(estado, pageable);
        } else if (tipo != null) {
            result = unidadService.findByTipo(tipo, pageable);
        } else {
            result = unidadService.findAll(pageable);
        }
        
        return ResponseEntity.ok(ApiResponse.ok(result, "Unidades obtenidas"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('UNIDADES_LEER')")
    @Operation(summary = "Obtener unidad por ID")
    public ResponseEntity<ApiResponse<UnidadResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(unidadService.findById(id), "Unidad encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('UNIDADES_CREAR')")
    @Operation(summary = "Crear una nueva unidad")
    public ResponseEntity<ApiResponse<UnidadResponse>> crear(@Valid @RequestBody UnidadRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(unidadService.create(request), "Unidad creada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UNIDADES_EDITAR')")
    @Operation(summary = "Actualizar una unidad existente")
    public ResponseEntity<ApiResponse<UnidadResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UnidadRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(unidadService.update(id, request), "Unidad actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('UNIDADES_ELIMINAR')")
    @Operation(summary = "Eliminar una unidad")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        unidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
