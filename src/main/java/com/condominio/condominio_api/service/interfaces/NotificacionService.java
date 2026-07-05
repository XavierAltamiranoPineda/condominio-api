package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.NotificacionRequest;
import com.condominio.condominio_api.dto.response.NotificacionResponse;
import com.condominio.condominio_api.entity.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificacionService {
    NotificacionResponse findById(Long id);
    Page<NotificacionResponse> findByPersonaId(Long personaId, Pageable pageable);
    NotificacionResponse create(NotificacionRequest request);
    NotificacionResponse marcarComoEnviada(Long id);
    NotificacionResponse marcarComoLeida(Long id);
}
