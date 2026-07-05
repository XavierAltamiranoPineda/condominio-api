package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.TicketComentarioRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.TicketComentarioResponse;
import com.condominio.condominio_api.service.interfaces.TicketComentarioService;
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
@RequestMapping("/api/v1/tickets/{ticketId}/comentarios")
@RequiredArgsConstructor
@Tag(name = "Comentarios de Tickets", description = "Gestión de la conversación dentro de un ticket")
public class TicketComentarioController {

    private final TicketComentarioService comentarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('TICKETS_LEER')")
    @Operation(summary = "Listar comentarios de un ticket paginados")
    public ResponseEntity<ApiResponse<Page<TicketComentarioResponse>>> listar(
            @PathVariable Long ticketId,
            @PageableDefault(size = 20, sort = "fecha", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(comentarioService.findByTicketId(ticketId, pageable), "Comentarios obtenidos"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TICKETS_EDITAR')")
    @Operation(summary = "Añadir un comentario al ticket")
    public ResponseEntity<ApiResponse<TicketComentarioResponse>> crear(
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketComentarioRequest request) {
        
        request.setTicketId(ticketId);
        return ResponseEntity.status(201)
                .body(ApiResponse.created(comentarioService.create(request), "Comentario añadido exitosamente"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TICKETS_ELIMINAR')")
    @Operation(summary = "Eliminar un comentario")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long ticketId,
            @PathVariable Long id) {
        comentarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
