package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.RolRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.RolDetalleResponse;
import com.condominio.condominio_api.dto.response.RolResponse;
import com.condominio.condominio_api.service.interfaces.RolService;
import com.condominio.condominio_api.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Gestión de roles del sistema")
public class RolController {

    private final RolService rolService;

    @GetMapping
    @PreAuthorize("hasAuthority('USUARIOS_LEER')")
    @Operation(summary = "Listar todos los roles")
    public ResponseEntity<ApiResponse<List<RolResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(rolService.findAll(), "Roles obtenidos"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_LEER')")
    @Operation(summary = "Obtener rol por ID con sus permisos")
    public ResponseEntity<ApiResponse<RolDetalleResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(rolService.findById(id), "Rol encontrado"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo rol")
    public ResponseEntity<ApiResponse<RolResponse>> crear(@Valid @RequestBody RolRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(rolService.create(request), "Rol creado exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un rol existente")
    public ResponseEntity<ApiResponse<RolResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(rolService.update(id, request), "Rol actualizado"));
    }

    @PostMapping("/{rolId}/permisos/{permisoId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Asignar un permiso a un rol")
    public ResponseEntity<ApiResponse<Void>> asignarPermiso(
            @PathVariable Long rolId,
            @PathVariable Long permisoId) {
        rolService.assignPermiso(rolId, permisoId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Permiso asignado"));
    }

    @DeleteMapping("/{rolId}/permisos/{permisoId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Revocar un permiso de un rol")
    public ResponseEntity<Void> revocarPermiso(
            @PathVariable Long rolId,
            @PathVariable Long permisoId) {
        rolService.revokePermiso(rolId, permisoId);
        return ResponseEntity.noContent().build();
    }
}
