package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.VehiculoRequest;
import com.condominio.condominio_api.dto.response.VehiculoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface VehiculoService {
    VehiculoResponse findById(Long id);
    List<VehiculoResponse> findByUnidadId(Long unidadId);
    Page<VehiculoResponse> findByCondominioId(Long condominioId, Pageable pageable);
    VehiculoResponse create(VehiculoRequest request);
    VehiculoResponse update(Long id, VehiculoRequest request);
    void delete(Long id);
}
