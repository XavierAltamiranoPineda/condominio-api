package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.TicketRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.HistorialTicketResponse;
import com.condominio.condominio_api.dto.response.TicketResponse;
import com.condominio.condominio_api.service.interfaces.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Gestión de tickets, incidencias y mantenimiento")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasAuthority('TICKETS_LEER')")
    @Operation(summary = "Listar tickets paginados")
    public ResponseEntity<ApiResponse<Page<TicketResponse>>> listar(
            @PageableDefault(size = 20, sort = "fechaCreacion", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.findAll(pageable), "Tickets obtenidos"));
    }

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('TICKETS_LEER')")
    @Operation(summary = "Listar tickets por condominio")
    public ResponseEntity<ApiResponse<Page<TicketResponse>>> listarPorCondominio(
            @PathVariable Long condominioId,
            @PageableDefault(size = 20, sort = "fechaCreacion", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.findByCondominioId(condominioId, pageable), "Tickets del condominio obtenidos"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TICKETS_LEER')")
    @Operation(summary = "Obtener ticket por ID")
    public ResponseEntity<ApiResponse<TicketResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.findById(id), "Ticket encontrado"));
    }

    @GetMapping("/{id}/historial")
    @PreAuthorize("hasAuthority('TICKETS_LEER')")
    @Operation(summary = "Obtener historial de estados de un ticket")
    public ResponseEntity<ApiResponse<List<HistorialTicketResponse>>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.getHistorial(id), "Historial del ticket obtenido"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TICKETS_CREAR')")
    @Operation(summary = "Crear un nuevo ticket")
    public ResponseEntity<ApiResponse<TicketResponse>> crear(@Valid @RequestBody TicketRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(ticketService.create(request), "Ticket creado exitosamente"));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('TICKETS_CREAR')")
    @Operation(summary = "Crear un ticket con archivos adjuntos")
    public ResponseEntity<ApiResponse<TicketResponse>> crearConArchivos(
            @Valid @RequestPart("request") TicketRequest request,
            @RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(ticketService.createWithArchivos(request, files), "Ticket con archivos creado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TICKETS_EDITAR')")
    @Operation(summary = "Actualizar información básica del ticket")
    public ResponseEntity<ApiResponse<TicketResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.update(id, request), "Ticket actualizado"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('TICKETS_EDITAR')")
    @Operation(summary = "Cambiar el estado de un ticket")
    public ResponseEntity<ApiResponse<TicketResponse>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Long nuevoEstadoId,
            @RequestParam(required = false) String comentario) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.updateEstado(id, nuevoEstadoId, comentario), "Estado del ticket actualizado"));
    }

    @PatchMapping("/{id}/tecnico")
    @PreAuthorize("hasAuthority('TICKETS_EDITAR')")
    @Operation(summary = "Asignar o cambiar el técnico de un ticket")
    public ResponseEntity<ApiResponse<TicketResponse>> asignarTecnico(
            @PathVariable Long id,
            @RequestParam Long tecnicoId) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.updateTecnico(id, tecnicoId), "Técnico asignado al ticket"));
    }

    @PostMapping(value = "/{id}/adjuntos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('TICKETS_EDITAR')")
    @Operation(summary = "Añadir un archivo adjunto al ticket")
    public ResponseEntity<ApiResponse<TicketResponse>> adjuntarArchivo(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.attachFile(id, file), "Archivo adjuntado exitosamente"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TICKETS_ELIMINAR')")
    @Operation(summary = "Eliminar un ticket")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
