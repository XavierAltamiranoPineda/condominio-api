package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.ReciboRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.ReciboResponse;
import com.condominio.condominio_api.service.interfaces.ReciboService;
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
@RequestMapping("/api/v1/recibos")
@RequiredArgsConstructor
@Tag(name = "Recibos", description = "Gestión de recibos generados por pagos realizados")
public class ReciboController {

    private final ReciboService reciboService;

    @GetMapping
    @PreAuthorize("hasAuthority('RECIBOS_LEER')")
    @Operation(summary = "Listar recibos paginados")
    public ResponseEntity<ApiResponse<Page<ReciboResponse>>> listar(
            @PageableDefault(size = 20, sort = "numero", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(reciboService.findAll(pageable), "Recibos obtenidos"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RECIBOS_LEER')")
    @Operation(summary = "Obtener recibo por ID")
    public ResponseEntity<ApiResponse<ReciboResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(reciboService.findById(id), "Recibo encontrado"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RECIBOS_CREAR')")
    @Operation(summary = "Crear un nuevo recibo")
    public ResponseEntity<ApiResponse<ReciboResponse>> crear(@Valid @RequestBody ReciboRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(reciboService.create(request), "Recibo creado exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RECIBOS_EDITAR')")
    @Operation(summary = "Actualizar un recibo existente")
    public ResponseEntity<ApiResponse<ReciboResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReciboRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(reciboService.update(id, request), "Recibo actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RECIBOS_ELIMINAR')")
    @Operation(summary = "Eliminar un recibo")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reciboService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
