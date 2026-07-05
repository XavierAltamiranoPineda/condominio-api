package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.ReciboRequest;
import com.condominio.condominio_api.dto.response.ReciboResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ReciboService {

    @Transactional(readOnly = true)
    ReciboResponse findById(Long id);

    @Transactional(readOnly = true)
    Page<ReciboResponse> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Page<ReciboResponse> findByPagoId(Long pagoId, Pageable pageable);

    @Transactional
    ReciboResponse create(ReciboRequest request);

    @Transactional
    ReciboResponse createWithFile(Long pagoId, String numero, org.springframework.web.multipart.MultipartFile file);

    @Transactional
    ReciboResponse update(Long id, ReciboRequest request);

    @Transactional
    void delete(Long id);

    org.springframework.core.io.Resource getArchivoResource(Long reciboId);
}
