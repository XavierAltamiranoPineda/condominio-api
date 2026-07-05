package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.PagoRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.PagoResponse;
import com.condominio.condominio_api.service.interfaces.PagoService;
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
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Gestión de pagos de cuotas y recargos")
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    @PreAuthorize("hasAuthority('PAGOS_LEER')")
    @Operation(summary = "Listar pagos paginados")
    public ResponseEntity<ApiResponse<Page<PagoResponse>>> listar(
            @PageableDefault(size = 20, sort = "fecha", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(pagoService.findAll(pageable), "Pagos obtenidos"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAGOS_LEER')")
    @Operation(summary = "Obtener pago por ID")
    public ResponseEntity<ApiResponse<PagoResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(pagoService.findById(id), "Pago encontrado"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PAGOS_CREAR')")
    @Operation(summary = "Registrar un nuevo pago")
    public ResponseEntity<ApiResponse<PagoResponse>> crear(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(pagoService.create(request), "Pago registrado exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PAGOS_EDITAR')")
    @Operation(summary = "Actualizar un pago existente")
    public ResponseEntity<ApiResponse<PagoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(pagoService.update(id, request), "Pago actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PAGOS_ELIMINAR')")
    @Operation(summary = "Eliminar o anular un pago")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
