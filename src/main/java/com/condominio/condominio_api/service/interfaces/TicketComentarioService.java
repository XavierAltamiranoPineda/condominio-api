package com.condominio.condominio_api.service.interfaces;

import com.condominio.condominio_api.dto.request.TicketComentarioRequest;
import com.condominio.condominio_api.dto.response.TicketComentarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketComentarioService {
    Page<TicketComentarioResponse> findByTicketId(Long ticketId, Pageable pageable);
    TicketComentarioResponse create(TicketComentarioRequest request);
    void delete(Long id);
}
