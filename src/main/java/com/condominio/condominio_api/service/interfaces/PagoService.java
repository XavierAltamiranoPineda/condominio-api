package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.PagoRequest;
import com.condominio.condominio_api.dto.response.PagoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface PagoService {

    @Transactional(readOnly = true)
    PagoResponse findById(Long id);

    @Transactional(readOnly = true)
    Page<PagoResponse> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Page<PagoResponse> findByCuotaId(Long cuotaId, Pageable pageable);

    @Transactional
    PagoResponse create(PagoRequest request);

    @Transactional
    PagoResponse update(Long id, PagoRequest request);

    @Transactional
    void delete(Long id);
}
