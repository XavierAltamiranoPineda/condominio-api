package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.UsuarioRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.UsuarioResponse;
import com.condominio.condominio_api.service.interfaces.UsuarioService;
import com.condominio.condominio_api.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('USUARIOS_LEER')")
    @Operation(summary = "Listar usuarios de forma paginada")
    public ResponseEntity<ApiResponse<Page<UsuarioResponse>>> listar(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.findAll(pageable), "Usuarios obtenidos"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_LEER')")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.findById(id), "Usuario encontrado"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USUARIOS_CREAR')")
    @Operation(summary = "Crear un nuevo usuario de sistema para una persona")
    public ResponseEntity<ApiResponse<UsuarioResponse>> crear(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(usuarioService.create(request), "Usuario creado con éxito"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_EDITAR')")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.update(id, request), "Usuario actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_ELIMINAR')")
    @Operation(summary = "Eliminar usuario del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuarioId}/roles/{rolId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Asignar un rol a un usuario")
    public ResponseEntity<ApiResponse<Void>> asignarRol(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId) {
        usuarioService.assignRol(usuarioId, rolId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Rol asignado con éxito"));
    }

    @DeleteMapping("/{usuarioId}/roles/{rolId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Revocar un rol de un usuario")
    public ResponseEntity<Void> revocarRol(
            @PathVariable Long usuarioId,
            @PathVariable Long rolId) {
        usuarioService.revokeRol(usuarioId, rolId);
        return ResponseEntity.noContent().build();
    }
}
