package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.request.VotacionRequest;
import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.ResultadosVotacionResponse;
import com.condominio.condominio_api.dto.response.VotacionResponse;
import com.condominio.condominio_api.service.interfaces.VotacionService;
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
@RequestMapping("/api/v1/votaciones")
@RequiredArgsConstructor
@Tag(name = "Votaciones", description = "Gestión de votos para asambleas en curso")
public class VotacionController {

    private final VotacionService votacionService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_LEER')")
    @Operation(summary = "Obtener un voto específico por ID")
    public ResponseEntity<ApiResponse<VotacionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(votacionService.findById(id), "Voto encontrado"));
    }

    @GetMapping("/asamblea/{asambleaId}")
    @PreAuthorize("hasAuthority('ASAMBLEAS_LEER')")
    @Operation(summary = "Listar todos los votos de una asamblea (auditoría)")
    public ResponseEntity<ApiResponse<Page<VotacionResponse>>> getByAsambleaId(
            @PathVariable Long asambleaId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(votacionService.findByAsambleaId(asambleaId, pageable), "Votos obtenidos"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ASAMBLEAS_CREAR')") // O un permiso 'VOTAR' específico si lo hubiera
    @Operation(summary = "Emitir un voto (A_FAVOR, EN_CONTRA, ABSTENCION)")
    public ResponseEntity<ApiResponse<VotacionResponse>> emitirVoto(@Valid @RequestBody VotacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(votacionService.emitirVoto(request), "Voto registrado exitosamente"));
    }

    @GetMapping("/asamblea/{asambleaId}/resultados")
    @PreAuthorize("hasAuthority('ASAMBLEAS_LEER')")
    @Operation(summary = "Obtener escrutinio/resultados consolidados de la asamblea")
    public ResponseEntity<ApiResponse<ResultadosVotacionResponse>> obtenerResultados(@PathVariable Long asambleaId) {
        return ResponseEntity.ok(ApiResponse.ok(votacionService.obtenerResultados(asambleaId), "Resultados obtenidos"));
    }
}
