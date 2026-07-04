package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.UnidadRequest;
import com.condominio.condominio_api.dto.response.UnidadResponse;
import com.condominio.condominio_api.entity.enums.TipoUnidadEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface UnidadService {

    @Transactional(readOnly = true)
    UnidadResponse findById(Long id);

    @Transactional(readOnly = true)
    Page<UnidadResponse> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Page<UnidadResponse> findByEstado(String estado, Pageable pageable);

    @Transactional(readOnly = true)
    Page<UnidadResponse> findByTipo(TipoUnidadEnum tipo, Pageable pageable);

    @Transactional(readOnly = true)
    Page<UnidadResponse> findByTorreId(Long torreId, Pageable pageable);

    @Transactional
    UnidadResponse create(UnidadRequest request);

    @Transactional
    UnidadResponse update(Long id, UnidadRequest request);

    @Transactional
    void delete(Long id);
}
