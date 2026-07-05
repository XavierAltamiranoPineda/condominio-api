package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.MultaRequest;
import com.condominio.condominio_api.dto.response.MultaResponse;
import com.condominio.condominio_api.entity.Multa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MultaService {
    MultaResponse findById(Long id);
    Page<MultaResponse> findByCondominioId(Long condominioId, Pageable pageable);
    Page<MultaResponse> findByPersonaId(Long personaId, Pageable pageable);
    MultaResponse create(MultaRequest request);
    MultaResponse update(Long id, MultaRequest request);
    MultaResponse cambiarEstado(Long id, Multa.EstadoMulta nuevoEstado);
    void delete(Long id);
}
