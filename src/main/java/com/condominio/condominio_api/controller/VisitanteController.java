package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.VisitanteRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.VisitanteResponse;
import com.condominio.condominio_api.service.interfaces.VisitanteService;
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
@RequestMapping("/api/v1/visitantes")
@RequiredArgsConstructor
@Tag(name = "Visitantes", description = "Gestión del catálogo de visitantes (personas externas)")
public class VisitanteController {

    private final VisitanteService visitanteService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_LEER')")
    @Operation(summary = "Obtener visitante por ID")
    public ResponseEntity<ApiResponse<VisitanteResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(visitanteService.findById(id), "Visitante encontrado"));
    }

    @GetMapping("/cedula/{cedula}")
    @PreAuthorize("hasAuthority('SEGURIDAD_LEER')")
    @Operation(summary = "Obtener visitante por cédula")
    public ResponseEntity<ApiResponse<VisitanteResponse>> getByCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(ApiResponse.ok(visitanteService.findByCedula(cedula), "Visitante encontrado"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SEGURIDAD_LEER')")
    @Operation(summary = "Listar todos los visitantes")
    public ResponseEntity<ApiResponse<Page<VisitanteResponse>>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(visitanteService.findAll(pageable), "Visitantes obtenidos"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SEGURIDAD_CREAR')")
    @Operation(summary = "Registrar nuevo visitante en el catálogo")
    public ResponseEntity<ApiResponse<VisitanteResponse>> create(@Valid @RequestBody VisitanteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(visitanteService.create(request), "Visitante registrado exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_EDITAR')")
    @Operation(summary = "Actualizar datos de un visitante")
    public ResponseEntity<ApiResponse<VisitanteResponse>> update(
            @PathVariable Long id, @Valid @RequestBody VisitanteRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(visitanteService.update(id, request), "Visitante actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_ELIMINAR')")
    @Operation(summary = "Eliminar visitante del catálogo")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        visitanteService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Visitante eliminado exitosamente"));
    }
}
