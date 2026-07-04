package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.TorreRequest;
import com.condominio.condominio_api.dto.response.TorreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface TorreService {

    @Transactional(readOnly = true)
    TorreResponse findById(Long id);

    @Transactional(readOnly = true)
    Page<TorreResponse> findAll(Pageable pageable);

    @Transactional
    TorreResponse create(TorreRequest request);

    @Transactional
    TorreResponse update(Long id, TorreRequest request);

    @Transactional
    void delete(Long id);
}
