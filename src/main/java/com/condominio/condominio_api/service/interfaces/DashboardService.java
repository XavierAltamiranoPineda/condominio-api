package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboardMetrics(Long condominioId);
}
