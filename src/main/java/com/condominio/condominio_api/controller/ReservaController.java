package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.ReservaRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.ReservaResponse;
import com.condominio.condominio_api.service.interfaces.ReservaService;
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
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Gestión de reservas de áreas comunes")
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RESERVAS_LEER')")
    @Operation(summary = "Obtener una reserva por su ID")
    public ResponseEntity<ApiResponse<ReservaResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.findById(id), "Reserva encontrada"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('RESERVAS_LEER')")
    @Operation(summary = "Listar todas las reservas de un condominio")
    public ResponseEntity<ApiResponse<Page<ReservaResponse>>> getByCondominioId(
            @PathVariable Long condominioId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.findByCondominioId(condominioId, pageable), "Reservas obtenidas"));
    }

    @GetMapping("/area/{areaId}")
    @PreAuthorize("hasAuthority('RESERVAS_LEER')")
    @Operation(summary = "Listar las reservas de un área común específica")
    public ResponseEntity<ApiResponse<Page<ReservaResponse>>> getByAreaId(
            @PathVariable Long areaId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.findByAreaId(areaId, pageable), "Reservas del área obtenidas"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RESERVAS_CREAR')")
    @Operation(summary = "Crear nueva reserva de área común")
    public ResponseEntity<ApiResponse<ReservaResponse>> create(@Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(reservaService.create(request), "Reserva creada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RESERVAS_EDITAR')")
    @Operation(summary = "Actualizar datos de una reserva (reprogramar)")
    public ResponseEntity<ApiResponse<ReservaResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.update(id, request), "Reserva actualizada exitosamente"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('RESERVAS_EDITAR')")
    @Operation(summary = "Aprobar o rechazar una reserva (cambiar estado)")
    public ResponseEntity<ApiResponse<ReservaResponse>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Long estadoId,
            @RequestParam(required = false) Long usuarioAprobadorId) {
        return ResponseEntity.ok(ApiResponse.ok(reservaService.cambiarEstado(id, estadoId, usuarioAprobadorId), "Estado de reserva actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RESERVAS_ELIMINAR')")
    @Operation(summary = "Eliminar reserva")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Reserva eliminada exitosamente"));
    }
}
