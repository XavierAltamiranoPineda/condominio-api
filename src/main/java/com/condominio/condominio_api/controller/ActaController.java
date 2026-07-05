package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.ActaRequest;
import com.condominio.condominio_api.dto.response.ActaResponse;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.service.interfaces.ActaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/actas")
@RequiredArgsConstructor
@Tag(name = "Actas", description = "Gestión de actas resolutivas de asambleas finalizadas")
public class ActaController {

    private final ActaService actaService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_LEER')")
    @Operation(summary = "Obtener acta por ID")
    public ResponseEntity<ApiResponse<ActaResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(actaService.findById(id), "Acta encontrada"));
    }

    @GetMapping("/asamblea/{asambleaId}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_LEER')")
    @Operation(summary = "Obtener el acta de una asamblea")
    public ResponseEntity<ApiResponse<ActaResponse>> getByAsambleaId(@PathVariable Long asambleaId) {
        return ResponseEntity.ok(ApiResponse.ok(actaService.findByAsambleaId(asambleaId), "Acta obtenida"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ASAMBLEAS_CREAR')")
    @Operation(summary = "Registrar el acta de una asamblea finalizada")
    public ResponseEntity<ApiResponse<ActaResponse>> create(@Valid @RequestBody ActaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(actaService.create(request), "Acta registrada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_EDITAR')")
    @Operation(summary = "Modificar el contenido o adjuntos de un acta")
    public ResponseEntity<ApiResponse<ActaResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ActaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(actaService.update(id, request), "Acta actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_ELIMINAR')")
    @Operation(summary = "Eliminar acta")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        actaService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Acta eliminada"));
    }
}
