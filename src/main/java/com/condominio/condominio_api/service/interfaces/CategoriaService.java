package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.CategoriaRequest;
import com.condominio.condominio_api.dto.response.CategoriaResponse;
import java.util.List;

public interface CategoriaService {
    CategoriaResponse findById(Long id);
    List<CategoriaResponse> findAll();
    CategoriaResponse create(CategoriaRequest request);
    CategoriaResponse update(Long id, CategoriaRequest request);
    void delete(Long id);
}
