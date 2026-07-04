package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.PersonaUnidadRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.PersonaUnidadResponse;
import com.condominio.condominio_api.service.interfaces.PersonaUnidadService;
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
@RequestMapping("/api/v1/personas-unidades")
@RequiredArgsConstructor
@Tag(name = "Personas Unidades", description = "Relación entre personas y unidades (quién vive o es dueño de qué)")
public class PersonaUnidadController {

    private final PersonaUnidadService personaUnidadService;

    @GetMapping
    @PreAuthorize("hasAuthority('UNIDADES_LEER') or hasAuthority('PERSONAS_LEER')")
    @Operation(summary = "Listar relaciones persona-unidad paginadas")
    public ResponseEntity<ApiResponse<Page<PersonaUnidadResponse>>> listar(
            @PageableDefault(size = 20, sort = "fechaInicio", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(personaUnidadService.findAll(pageable), "Relaciones obtenidas"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('UNIDADES_LEER') or hasAuthority('PERSONAS_LEER')")
    @Operation(summary = "Obtener relación por ID")
    public ResponseEntity<ApiResponse<PersonaUnidadResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(personaUnidadService.findById(id), "Relación encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('UNIDADES_EDITAR') or hasAuthority('PERSONAS_EDITAR')")
    @Operation(summary = "Crear una nueva relación persona-unidad")
    public ResponseEntity<ApiResponse<PersonaUnidadResponse>> crear(@Valid @RequestBody PersonaUnidadRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(personaUnidadService.create(request), "Relación creada exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UNIDADES_EDITAR') or hasAuthority('PERSONAS_EDITAR')")
    @Operation(summary = "Actualizar una relación existente")
    public ResponseEntity<ApiResponse<PersonaUnidadResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonaUnidadRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(personaUnidadService.update(id, request), "Relación actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('UNIDADES_EDITAR') or hasAuthority('PERSONAS_EDITAR')")
    @Operation(summary = "Eliminar una relación")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        personaUnidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
