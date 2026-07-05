package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.ComunicadoLecturaRequest;
import com.condominio.condominio_api.dto.request.ComunicadoRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.ComunicadoResponse;
import com.condominio.condominio_api.service.interfaces.ComunicadoService;
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
@RequestMapping("/api/v1/comunicados")
@RequiredArgsConstructor
@Tag(name = "Comunicados", description = "Gestión del tablón de anuncios y lecturas")
public class ComunicadoController {

    private final ComunicadoService comunicadoService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Cualquiera autenticado puede intentar leer un comunicado
    @Operation(summary = "Obtener comunicado por ID")
    public ResponseEntity<ApiResponse<ComunicadoResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(comunicadoService.findById(id), "Comunicado encontrado"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar todos los comunicados")
    public ResponseEntity<ApiResponse<Page<ComunicadoResponse>>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(comunicadoService.findAll(pageable), "Comunicados obtenidos"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')") // Asumiendo que solo admin crea comunicados o un permiso específico
    @Operation(summary = "Publicar nuevo comunicado")
    public ResponseEntity<ApiResponse<ComunicadoResponse>> create(@Valid @RequestBody ComunicadoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(comunicadoService.create(request), "Comunicado publicado exitosamente"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Actualizar comunicado")
    public ResponseEntity<ApiResponse<ComunicadoResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ComunicadoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(comunicadoService.update(id, request), "Comunicado actualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Eliminar comunicado")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        comunicadoService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Comunicado eliminado"));
    }

    @PostMapping("/lecturas")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marcar un comunicado como leído")
    public ResponseEntity<ApiResponse<Void>> marcarComoLeido(@Valid @RequestBody ComunicadoLecturaRequest request) {
        comunicadoService.marcarComoLeido(request);
        return ResponseEntity.ok(ApiResponse.ok(null, "Comunicado marcado como leído"));
    }
}
