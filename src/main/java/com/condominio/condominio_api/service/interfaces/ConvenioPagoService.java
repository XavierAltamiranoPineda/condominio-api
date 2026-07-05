package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.ConvenioPagoRequest;
import com.condominio.condominio_api.dto.response.ConvenioPagoResponse;
import com.condominio.condominio_api.entity.ConvenioPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConvenioPagoService {
    ConvenioPagoResponse findById(Long id);
    Page<ConvenioPagoResponse> findByCondominioId(Long condominioId, Pageable pageable);
    Page<ConvenioPagoResponse> findByPersonaId(Long personaId, Pageable pageable);
    ConvenioPagoResponse create(ConvenioPagoRequest request);
    ConvenioPagoResponse update(Long id, ConvenioPagoRequest request);
    ConvenioPagoResponse cambiarEstado(Long id, ConvenioPago.EstadoConvenio nuevoEstado);
    void delete(Long id);
}
