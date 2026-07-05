package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.MultaRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.MultaResponse;
import com.condominio.condominio_api.entity.Multa;
import com.condominio.condominio_api.service.interfaces.MultaService;
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
@RequestMapping("/api/v1/multas")
@RequiredArgsConstructor
@Tag(name = "Multas", description = "Gestión de infracciones y multas")
public class MultaController {

    private final MultaService multaService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('RESIDENTE')")
    @Operation(summary = "Obtener multa por ID")
    public ResponseEntity<ApiResponse<MultaResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(multaService.findById(id), "Multa encontrada"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Listar todas las multas del condominio")
    public ResponseEntity<ApiResponse<Page<MultaResponse>>> getByCondominioId(
            @PathVariable Long condominioId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(multaService.findByCondominioId(condominioId, pageable), "Multas obtenidas"));
    }

    @GetMapping("/persona/{personaId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('RESIDENTE')")
    @Operation(summary = "Listar multas de una persona")
    public ResponseEntity<ApiResponse<Page<MultaResponse>>> getByPersonaId(
            @PathVariable Long personaId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(multaService.findByPersonaId(personaId, pageable), "Multas obtenidas"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Registrar nueva multa")
    public ResponseEntity<ApiResponse<MultaResponse>> create(@Valid @RequestBody MultaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(multaService.create(request), "Multa registrada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Actualizar datos de la multa")
    public ResponseEntity<ApiResponse<MultaResponse>> update(
            @PathVariable Long id, @Valid @RequestBody MultaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(multaService.update(id, request), "Multa actualizada"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Cambiar estado de la multa (ej: FACTURADA, ANULADA)")
    public ResponseEntity<ApiResponse<MultaResponse>> cambiarEstado(
            @PathVariable Long id, @RequestParam Multa.EstadoMulta estado) {
        return ResponseEntity.ok(ApiResponse.ok(multaService.cambiarEstado(id, estado), "Estado actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Eliminar multa (solo si está REGISTRADA)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        multaService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Multa eliminada"));
    }
}
