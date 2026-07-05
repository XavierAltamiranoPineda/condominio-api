package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.ReciboRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.ReciboResponse;
import com.condominio.condominio_api.service.interfaces.ReciboService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Recibos", description = "Gestión de recibos generados por pagos realizados")
public class ReciboController {

    private final ReciboService reciboService;

    @GetMapping("/recibos")
    @PreAuthorize("hasAuthority('RECIBOS_LEER')")
    @Operation(summary = "Listar recibos paginados")
    public ResponseEntity<ApiResponse<Page<ReciboResponse>>> listar(
            @PageableDefault(size = 20, sort = "numero", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(reciboService.findAll(pageable), "Recibos obtenidos"));
    }

    @GetMapping("/recibos/{id}")
    @PreAuthorize("hasAuthority('RECIBOS_LEER')")
    @Operation(summary = "Obtener recibo por ID")
    public ResponseEntity<ApiResponse<ReciboResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(reciboService.findById(id), "Recibo encontrado"));
    }

    @PostMapping("/recibos")
    @PreAuthorize("hasAuthority('RECIBOS_CREAR')")
    @Operation(summary = "Crear un nuevo recibo sin archivo")
    public ResponseEntity<ApiResponse<ReciboResponse>> crear(@Valid @RequestBody ReciboRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(reciboService.create(request), "Recibo creado exitosamente"));
    }

    @PostMapping(value = "/pagos/{pagoId}/recibo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('RECIBOS_CREAR')")
    @Operation(summary = "Crear un recibo subiendo un archivo adjunto")
    public ResponseEntity<ApiResponse<ReciboResponse>> crearConArchivo(
            @PathVariable Long pagoId,
            @RequestParam("numero") String numero,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(reciboService.createWithFile(pagoId, numero, file), "Recibo con archivo creado"));
    }

    @GetMapping("/recibos/{id}/download")
    @PreAuthorize("hasAuthority('RECIBOS_LEER')")
    @Operation(summary = "Descargar el archivo asociado al recibo")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long id) {
        Resource file = reciboService.getArchivoResource(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PutMapping("/recibos/{id}")
    @PreAuthorize("hasAuthority('RECIBOS_EDITAR')")
    @Operation(summary = "Actualizar un recibo existente")
    public ResponseEntity<ApiResponse<ReciboResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReciboRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(reciboService.update(id, request), "Recibo actualizado"));
    }

    @DeleteMapping("/recibos/{id}")
    @PreAuthorize("hasAuthority('RECIBOS_ELIMINAR')")
    @Operation(summary = "Eliminar un recibo")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reciboService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
