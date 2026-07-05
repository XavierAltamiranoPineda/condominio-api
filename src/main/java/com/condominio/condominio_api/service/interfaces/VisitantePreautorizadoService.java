package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.VisitantePreautorizadoRequest;
import com.condominio.condominio_api.dto.response.VisitantePreautorizadoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitantePreautorizadoService {
    VisitantePreautorizadoResponse findById(Long id);
    Page<VisitantePreautorizadoResponse> findByCondominioId(Long condominioId, Pageable pageable);
    Page<VisitantePreautorizadoResponse> findByUnidadId(Long unidadId, Pageable pageable);
    VisitantePreautorizadoResponse create(VisitantePreautorizadoRequest request);
    void delete(Long id);
}
