package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.AreaComunRequest;
import com.condominio.condominio_api.dto.response.AreaComunResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AreaComunService {
    AreaComunResponse findById(Long id);
    Page<AreaComunResponse> findByCondominioId(Long condominioId, Pageable pageable);
    AreaComunResponse create(AreaComunRequest request);
    AreaComunResponse update(Long id, AreaComunRequest request);
    void delete(Long id);
}
