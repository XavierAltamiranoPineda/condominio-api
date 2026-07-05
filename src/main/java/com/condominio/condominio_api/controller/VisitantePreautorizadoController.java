package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.VisitantePreautorizadoRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.VisitantePreautorizadoResponse;
import com.condominio.condominio_api.service.interfaces.VisitantePreautorizadoService;
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
@RequestMapping("/api/v1/preautorizaciones")
@RequiredArgsConstructor
@Tag(name = "Visitantes Preautorizados", description = "Gestión de preautorizaciones por parte de los residentes")
public class VisitantePreautorizadoController {

    private final VisitantePreautorizadoService preautorizadoService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_LEER')")
    @Operation(summary = "Obtener preautorización por ID")
    public ResponseEntity<ApiResponse<VisitantePreautorizadoResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(preautorizadoService.findById(id), "Preautorización encontrada"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('SEGURIDAD_LEER')")
    @Operation(summary = "Listar preautorizaciones del condominio")
    public ResponseEntity<ApiResponse<Page<VisitantePreautorizadoResponse>>> getByCondominioId(
            @PathVariable Long condominioId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(preautorizadoService.findByCondominioId(condominioId, pageable), "Preautorizaciones obtenidas"));
    }

    @GetMapping("/unidad/{unidadId}")
    @PreAuthorize("hasAuthority('SEGURIDAD_LEER')")
    @Operation(summary = "Listar preautorizaciones de una unidad específica")
    public ResponseEntity<ApiResponse<Page<VisitantePreautorizadoResponse>>> getByUnidadId(
            @PathVariable Long unidadId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(preautorizadoService.findByUnidadId(unidadId, pageable), "Preautorizaciones de la unidad obtenidas"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SEGURIDAD_CREAR')") // Los residentes también deberían tener permiso
    @Operation(summary = "Crear nueva preautorización de visita")
    public ResponseEntity<ApiResponse<VisitantePreautorizadoResponse>> create(@Valid @RequestBody VisitantePreautorizadoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(preautorizadoService.create(request), "Visitante preautorizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_ELIMINAR')")
    @Operation(summary = "Eliminar preautorización (revocar)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        preautorizadoService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Preautorización revocada exitosamente"));
    }
}
