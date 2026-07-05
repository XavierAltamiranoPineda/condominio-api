package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.ActaRequest;
import com.condominio.condominio_api.dto.response.ActaResponse;

public interface ActaService {
    ActaResponse findById(Long id);
    ActaResponse findByAsambleaId(Long asambleaId);
    ActaResponse create(ActaRequest request);
    ActaResponse update(Long id, ActaRequest request);
    void delete(Long id);
}
