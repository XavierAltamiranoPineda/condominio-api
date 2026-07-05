package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.AreaComunRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.AreaComunResponse;
import com.condominio.condominio_api.service.interfaces.AreaComunService;
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
@RequestMapping("/api/v1/areas-comunes")
@RequiredArgsConstructor
@Tag(name = "Áreas Comunes", description = "CRUD de las amenidades del condominio")
public class AreaComunController {

    private final AreaComunService areaComunService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RESERVAS_LEER')")
    @Operation(summary = "Obtener un área común por su ID")
    public ResponseEntity<ApiResponse<AreaComunResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(areaComunService.findById(id), "Área común encontrada"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('RESERVAS_LEER')")
    @Operation(summary = "Listar áreas comunes por condominio")
    public ResponseEntity<ApiResponse<Page<AreaComunResponse>>> getByCondominioId(
            @PathVariable Long condominioId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(areaComunService.findByCondominioId(condominioId, pageable), "Áreas comunes obtenidas"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RESERVAS_CREAR')") // O un permiso más específico si se requiere (ej. AREAS_CREAR)
    @Operation(summary = "Crear nueva área común")
    public ResponseEntity<ApiResponse<AreaComunResponse>> create(@Valid @RequestBody AreaComunRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(areaComunService.create(request), "Área común creada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RESERVAS_EDITAR')")
    @Operation(summary = "Actualizar área común")
    public ResponseEntity<ApiResponse<AreaComunResponse>> update(
            @PathVariable Long id, @Valid @RequestBody AreaComunRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(areaComunService.update(id, request), "Área común actualizada exitosamente"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RESERVAS_ELIMINAR')")
    @Operation(summary = "Eliminar área común")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        areaComunService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Área común eliminada exitosamente"));
    }
}
