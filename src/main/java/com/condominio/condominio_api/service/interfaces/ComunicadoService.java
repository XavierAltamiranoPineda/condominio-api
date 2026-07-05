package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.ComunicadoLecturaRequest;
import com.condominio.condominio_api.dto.request.ComunicadoRequest;
import com.condominio.condominio_api.dto.response.ComunicadoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComunicadoService {
    ComunicadoResponse findById(Long id);
    Page<ComunicadoResponse> findAll(Pageable pageable);
    ComunicadoResponse create(ComunicadoRequest request);
    ComunicadoResponse update(Long id, ComunicadoRequest request);
    void delete(Long id);
    
    void marcarComoLeido(ComunicadoLecturaRequest request);
    boolean estaLeido(Long comunicadoId, Long personaId);
}
