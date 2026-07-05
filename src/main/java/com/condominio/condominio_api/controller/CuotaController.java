package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.CuotaRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.CuotaResponse;
import com.condominio.condominio_api.service.interfaces.CuotaService;
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
@RequestMapping("/api/v1/cuotas")
@RequiredArgsConstructor
@Tag(name = "Cuotas", description = "Gestión de cuotas ordinarias, extraordinarias y multas")
public class CuotaController {

    private final CuotaService cuotaService;

    @GetMapping
    @PreAuthorize("hasAuthority('CUOTAS_LEER')")
    @Operation(summary = "Listar cuotas paginadas")
    public ResponseEntity<ApiResponse<Page<CuotaResponse>>> listar(
            @PageableDefault(size = 20, sort = "fechaVencimiento", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(cuotaService.findAll(pageable), "Cuotas obtenidas"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUOTAS_LEER')")
    @Operation(summary = "Obtener cuota por ID")
    public ResponseEntity<ApiResponse<CuotaResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(cuotaService.findById(id), "Cuota encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CUOTAS_CREAR')")
    @Operation(summary = "Crear una nueva cuota")
    public ResponseEntity<ApiResponse<CuotaResponse>> crear(@Valid @RequestBody CuotaRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(cuotaService.create(request), "Cuota creada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CUOTAS_EDITAR')")
    @Operation(summary = "Actualizar una cuota existente")
    public ResponseEntity<ApiResponse<CuotaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CuotaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(cuotaService.update(id, request), "Cuota actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CUOTAS_ELIMINAR')")
    @Operation(summary = "Eliminar una cuota")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuotaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
