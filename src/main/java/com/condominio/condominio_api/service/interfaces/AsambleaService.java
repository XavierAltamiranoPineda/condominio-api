package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.AsambleaRequest;
import com.condominio.condominio_api.dto.response.AsambleaResponse;
import com.condominio.condominio_api.entity.Asamblea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AsambleaService {
    AsambleaResponse findById(Long id);
    Page<AsambleaResponse> findByCondominioId(Long condominioId, Pageable pageable);
    AsambleaResponse create(AsambleaRequest request);
    AsambleaResponse update(Long id, AsambleaRequest request);
    AsambleaResponse cambiarEstado(Long id, Asamblea.EstadoAsamblea nuevoEstado);
    void delete(Long id);
}
