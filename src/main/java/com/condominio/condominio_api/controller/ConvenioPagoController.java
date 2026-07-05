package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.ConvenioPagoRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.ConvenioPagoResponse;
import com.condominio.condominio_api.entity.ConvenioPago;
import com.condominio.condominio_api.service.interfaces.ConvenioPagoService;
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
@RequestMapping("/api/v1/convenios")
@RequiredArgsConstructor
@Tag(name = "Convenios de Pago", description = "Gestión de convenios de pago a plazos")
public class ConvenioPagoController {

    private final ConvenioPagoService convenioService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('RESIDENTE')")
    @Operation(summary = "Obtener convenio por ID")
    public ResponseEntity<ApiResponse<ConvenioPagoResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(convenioService.findById(id), "Convenio encontrado"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Listar convenios del condominio")
    public ResponseEntity<ApiResponse<Page<ConvenioPagoResponse>>> getByCondominioId(
            @PathVariable Long condominioId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(convenioService.findByCondominioId(condominioId, pageable), "Convenios obtenidos"));
    }

    @GetMapping("/persona/{personaId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('RESIDENTE')")
    @Operation(summary = "Listar convenios de una persona")
    public ResponseEntity<ApiResponse<Page<ConvenioPagoResponse>>> getByPersonaId(
            @PathVariable Long personaId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(convenioService.findByPersonaId(personaId, pageable), "Convenios obtenidos"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Crear nuevo convenio de pago")
    public ResponseEntity<ApiResponse<ConvenioPagoResponse>> create(@Valid @RequestBody ConvenioPagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(convenioService.create(request), "Convenio creado exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Actualizar convenio de pago")
    public ResponseEntity<ApiResponse<ConvenioPagoResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ConvenioPagoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(convenioService.update(id, request), "Convenio actualizado"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Cambiar estado del convenio (COMPLETADO, INCUMPLIDO)")
    public ResponseEntity<ApiResponse<ConvenioPagoResponse>> cambiarEstado(
            @PathVariable Long id, @RequestParam ConvenioPago.EstadoConvenio estado) {
        return ResponseEntity.ok(ApiResponse.ok(convenioService.cambiarEstado(id, estado), "Estado del convenio actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Eliminar convenio (solo ACTIVO)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        convenioService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Convenio eliminado"));
    }
}
