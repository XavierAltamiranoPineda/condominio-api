package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.VehiculoRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.VehiculoResponse;
import com.condominio.condominio_api.service.interfaces.VehiculoService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehículos", description = "Gestión de vehículos de unidades y residentes")
public class VehiculoController {

    private final VehiculoService service;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener vehículo por ID")
    public ResponseEntity<ApiResponse<VehiculoResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id), "Vehículo encontrado"));
    }

    @GetMapping("/unidad/{unidadId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener vehículos de una unidad")
    public ResponseEntity<ApiResponse<List<VehiculoResponse>>> getByUnidadId(@PathVariable Long unidadId) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByUnidadId(unidadId), "Vehículos obtenidos"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Obtener vehículos paginados por condominio")
    public ResponseEntity<ApiResponse<Page<VehiculoResponse>>> getByCondominioId(
            @PathVariable Long condominioId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByCondominioId(condominioId, pageable), "Vehículos obtenidos"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('RESIDENTE')")
    @Operation(summary = "Registrar nuevo vehículo")
    public ResponseEntity<ApiResponse<VehiculoResponse>> create(@Valid @RequestBody VehiculoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(service.create(request), "Vehículo registrado exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('RESIDENTE')")
    @Operation(summary = "Actualizar vehículo")
    public ResponseEntity<ApiResponse<VehiculoResponse>> update(
            @PathVariable Long id, @Valid @RequestBody VehiculoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request), "Vehículo actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('RESIDENTE')")
    @Operation(summary = "Eliminar vehículo")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Vehículo eliminado"));
    }
}
