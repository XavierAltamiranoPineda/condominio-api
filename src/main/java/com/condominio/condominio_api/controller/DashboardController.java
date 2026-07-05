package com.condominio.condominio_api.controller;

import com.condominio.condominio_api.dto.response.ApiResponse;
import com.condominio.condominio_api.dto.response.DashboardResponse;
import com.condominio.condominio_api.service.interfaces.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Indicadores y métricas agregadas del condominio")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/condominio/{condominioId}")
    @PreAuthorize("hasAuthority('REPORTES_LEER')")
    @Operation(summary = "Obtener todas las métricas del dashboard de un condominio")
    public ResponseEntity<ApiResponse<DashboardResponse>> getMetrics(@PathVariable Long condominioId) {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDashboardMetrics(condominioId), "Métricas del dashboard generadas exitosamente"));
    }
}
