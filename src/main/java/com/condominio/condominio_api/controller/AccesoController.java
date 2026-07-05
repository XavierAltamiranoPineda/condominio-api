package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.AccesoRequest;
import com.condominio.condominio_api.dto.response.AccesoResponse;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.service.interfaces.AccesoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/accesos")
@RequiredArgsConstructor
@Tag(name = "Control de Accesos", description = "Gestión de ingresos y salidas (uso de guardias)")
public class AccesoController {

    private final AccesoService accesoService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SEGURIDAD_LEER')")
    @Operation(summary = "Obtener acceso por ID")
    public ResponseEntity<ApiResponse<AccesoResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(accesoService.findById(id), "Acceso encontrado"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('SEGURIDAD_LEER')")
    @Operation(summary = "Listar accesos del condominio")
    public ResponseEntity<ApiResponse<Page<AccesoResponse>>> getByCondominioId(
            @PathVariable Long condominioId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(accesoService.findByCondominioId(condominioId, pageable), "Accesos obtenidos"));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SEGURIDAD_CREAR')")
    @Operation(summary = "Registrar ingreso (check-in)")
    public ResponseEntity<ApiResponse<AccesoResponse>> registrarIngreso(
            @Valid @RequestPart("acceso") AccesoRequest request,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(accesoService.registrarIngreso(request, foto), "Ingreso registrado exitosamente"));
    }

    @PatchMapping("/{id}/salida")
    @PreAuthorize("hasAuthority('SEGURIDAD_EDITAR')")
    @Operation(summary = "Registrar salida (check-out)")
    public ResponseEntity<ApiResponse<AccesoResponse>> registrarSalida(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(accesoService.registrarSalida(id), "Salida registrada exitosamente"));
    }

    @PostMapping("/denegado")
    @PreAuthorize("hasAuthority('SEGURIDAD_CREAR')")
    @Operation(summary = "Registrar acceso denegado")
    public ResponseEntity<ApiResponse<AccesoResponse>> registrarDenegado(@Valid @RequestBody AccesoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(accesoService.registrarDenegado(request), "Acceso denegado registrado exitosamente"));
    }
}
