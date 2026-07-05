package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.ParqueaderoRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.ParqueaderoResponse;
import com.condominio.condominio_api.entity.Parqueadero;
import com.condominio.condominio_api.service.interfaces.ParqueaderoService;
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
@RequestMapping("/api/v1/parqueaderos")
@RequiredArgsConstructor
@Tag(name = "Parqueaderos", description = "Gestión de cajones de estacionamiento")
public class ParqueaderoController {

    private final ParqueaderoService service;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener parqueadero por ID")
    public ResponseEntity<ApiResponse<ParqueaderoResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id), "Parqueadero encontrado"));
    }

    @GetMapping("/unidad/{unidadId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener parqueaderos de una unidad")
    public ResponseEntity<ApiResponse<List<ParqueaderoResponse>>> getByUnidadId(@PathVariable Long unidadId) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByUnidadId(unidadId), "Parqueaderos obtenidos"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Obtener parqueaderos paginados por condominio")
    public ResponseEntity<ApiResponse<Page<ParqueaderoResponse>>> getByCondominioId(
            @PathVariable Long condominioId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByCondominioId(condominioId, pageable), "Parqueaderos obtenidos"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Registrar nuevo parqueadero")
    public ResponseEntity<ApiResponse<ParqueaderoResponse>> create(@Valid @RequestBody ParqueaderoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(service.create(request), "Parqueadero registrado exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Actualizar parqueadero")
    public ResponseEntity<ApiResponse<ParqueaderoResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ParqueaderoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request), "Parqueadero actualizado"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GUARDIA')")
    @Operation(summary = "Cambiar estado del parqueadero (DISPONIBLE, OCUPADO)")
    public ResponseEntity<ApiResponse<ParqueaderoResponse>> cambiarEstado(
            @PathVariable Long id, @RequestParam Parqueadero.EstadoParqueadero estado) {
        return ResponseEntity.ok(ApiResponse.ok(service.cambiarEstado(id, estado), "Estado actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Eliminar parqueadero")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Parqueadero eliminado"));
    }
}
