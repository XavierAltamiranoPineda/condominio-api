package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.PersonaRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.PersonaResponse;
import com.condominio.condominio_api.service.interfaces.PersonaService;
import com.condominio.condominio_api.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/personas")
@RequiredArgsConstructor
@Tag(name = "Personas", description = "Gestión de personas del condominio")
public class PersonaController {

    private final PersonaService personaService;

    @GetMapping
    @PreAuthorize("hasAuthority('RESIDENTES_LEER')")
    @Operation(summary = "Listar todas las personas de forma paginada")
    public ResponseEntity<ApiResponse<Page<PersonaResponse>>> listar(
            @PageableDefault(size = 20, sort = "apellidos") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(personaService.findAll(pageable), "Personas obtenidas"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RESIDENTES_LEER')")
    @Operation(summary = "Obtener persona por ID")
    public ResponseEntity<ApiResponse<PersonaResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(personaService.findById(id), "Persona encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RESIDENTES_CREAR')")
    @Operation(summary = "Registrar una nueva persona")
    public ResponseEntity<ApiResponse<PersonaResponse>> crear(@Valid @RequestBody PersonaRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(personaService.create(request), "Persona registrada con éxito"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RESIDENTES_EDITAR')")
    @Operation(summary = "Actualizar datos de una persona")
    public ResponseEntity<ApiResponse<PersonaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(personaService.update(id, request), "Persona actualizada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RESIDENTES_ELIMINAR')")
    @Operation(summary = "Desactivar una persona lógicamente (estado INACTIVO)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        personaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
