package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.RolRequest;
import com.condominio.condominio_api.dto.response.RolDetalleResponse;
import com.condominio.condominio_api.dto.response.RolResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolService {

    @Transactional(readOnly = true)
    List<RolResponse> findAll();

    @Transactional(readOnly = true)
    RolDetalleResponse findById(Long id);

    @Transactional
    RolResponse create(RolRequest request);

    @Transactional
    RolResponse update(Long id, RolRequest request);

    @Transactional
    void assignPermiso(Long rolId, Long permisoId);

    @Transactional
    void revokePermiso(Long rolId, Long permisoId);
}
