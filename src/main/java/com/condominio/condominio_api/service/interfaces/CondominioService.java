package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.CondominioRequest;
import com.condominio.condominio_api.dto.response.CondominioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface CondominioService {

    @Transactional(readOnly = true)
    CondominioResponse findById(Long id);

    @Transactional(readOnly = true)
    Page<CondominioResponse> findAll(Pageable pageable);

    @Transactional
    CondominioResponse create(CondominioRequest request);

    @Transactional
    CondominioResponse update(Long id, CondominioRequest request);

    @Transactional
    void delete(Long id);
}
