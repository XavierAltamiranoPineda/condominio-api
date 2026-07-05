package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.ReservaRequest;
import com.condominio.condominio_api.dto.response.ReservaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservaService {
    ReservaResponse findById(Long id);
    Page<ReservaResponse> findByCondominioId(Long condominioId, Pageable pageable);
    Page<ReservaResponse> findByAreaId(Long areaId, Pageable pageable);
    ReservaResponse create(ReservaRequest request);
    ReservaResponse update(Long id, ReservaRequest request);
    ReservaResponse cambiarEstado(Long id, Long estadoId, Long usuarioAprobadorId);
    void delete(Long id);
}
