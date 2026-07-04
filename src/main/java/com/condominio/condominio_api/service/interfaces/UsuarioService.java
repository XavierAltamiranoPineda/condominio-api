package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.UsuarioRequest;
import com.condominio.condominio_api.dto.response.UsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioService {

    @Transactional(readOnly = true)
    Page<UsuarioResponse> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    UsuarioResponse findById(Long id);

    @Transactional
    UsuarioResponse create(UsuarioRequest request);

    @Transactional
    UsuarioResponse update(Long id, UsuarioRequest request);

    @Transactional
    void delete(Long id);

    @Transactional
    void assignRol(Long usuarioId, Long rolId);

    @Transactional
    void revokeRol(Long usuarioId, Long rolId);
}
