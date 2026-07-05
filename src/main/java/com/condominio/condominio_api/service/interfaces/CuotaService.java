package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.CuotaRequest;
import com.condominio.condominio_api.dto.response.CuotaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface CuotaService {

    @Transactional(readOnly = true)
    CuotaResponse findById(Long id);

    @Transactional(readOnly = true)
    Page<CuotaResponse> findAll(Pageable pageable);

    @Transactional
    CuotaResponse create(CuotaRequest request);

    @Transactional
    CuotaResponse update(Long id, CuotaRequest request);

    @Transactional
    void delete(Long id);
}
