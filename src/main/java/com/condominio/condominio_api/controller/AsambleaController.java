package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.AsambleaRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.AsambleaResponse;
import com.condominio.condominio_api.entity.Asamblea;
import com.condominio.condominio_api.service.interfaces.AsambleaService;
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
@RequestMapping("/api/v1/asambleas")
@RequiredArgsConstructor
@Tag(name = "Asambleas", description = "Gestión de juntas, reuniones y asambleas del condominio")
public class AsambleaController {

    private final AsambleaService asambleaService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_LEER')")
    @Operation(summary = "Obtener asamblea por ID")
    public ResponseEntity<ApiResponse<AsambleaResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(asambleaService.findById(id), "Asamblea encontrada"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_LEER')")
    @Operation(summary = "Listar asambleas de un condominio")
    public ResponseEntity<ApiResponse<Page<AsambleaResponse>>> getByCondominioId(
            @PathVariable Long condominioId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(asambleaService.findByCondominioId(condominioId, pageable), "Asambleas obtenidas"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ASAMBLEAS_CREAR')")
    @Operation(summary = "Programar nueva asamblea")
    public ResponseEntity<ApiResponse<AsambleaResponse>> create(@Valid @RequestBody AsambleaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(asambleaService.create(request), "Asamblea programada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_EDITAR')")
    @Operation(summary = "Actualizar datos de una asamblea programada")
    public ResponseEntity<ApiResponse<AsambleaResponse>> update(
            @PathVariable Long id, @Valid @RequestBody AsambleaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(asambleaService.update(id, request), "Asamblea actualizada"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('ASAMBLEAS_EDITAR')")
    @Operation(summary = "Cambiar estado de la asamblea (ej: EN_CURSO, FINALIZADA, CANCELADA)")
    public ResponseEntity<ApiResponse<AsambleaResponse>> cambiarEstado(
            @PathVariable Long id, @RequestParam Asamblea.EstadoAsamblea estado) {
        return ResponseEntity.ok(ApiResponse.ok(asambleaService.cambiarEstado(id, estado), "Estado de asamblea actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_ELIMINAR')")
    @Operation(summary = "Eliminar asamblea (solo si está programada)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        asambleaService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Asamblea eliminada"));
    }
}
