package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.VisitanteRequest;
import com.condominio.condominio_api.dto.response.VisitanteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitanteService {
    VisitanteResponse findById(Long id);
    VisitanteResponse findByCedula(String cedula);
    Page<VisitanteResponse> findAll(Pageable pageable);
    VisitanteResponse create(VisitanteRequest request);
    VisitanteResponse update(Long id, VisitanteRequest request);
    void delete(Long id);
}
