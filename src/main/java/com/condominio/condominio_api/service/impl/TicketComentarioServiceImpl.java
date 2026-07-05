package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.TicketComentarioRequest;
import com.condominio.condominio_api.dto.response.TicketComentarioResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Ticket;
import com.condominio.condominio_api.entity.TicketComentario;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.TicketComentarioMapper;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.repository.TicketComentarioRepository;
import com.condominio.condominio_api.repository.TicketRepository;
import com.condominio.condominio_api.service.interfaces.TicketComentarioService;
import com.condominio.condominio_api.event.TicketEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketComentarioServiceImpl implements TicketComentarioService {

    private final TicketComentarioRepository comentarioRepository;
    private final TicketRepository ticketRepository;
    private final PersonaRepository personaRepository;
    private final TicketComentarioMapper comentarioMapper;
    private final PostgresAuditInterceptor auditInterceptor;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Page<TicketComentarioResponse> findByTicketId(Long ticketId, Pageable pageable) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new ResourceNotFoundException("Ticket", "id", ticketId);
        }
        return comentarioRepository.findByTicketIdWithDetails(ticketId, pageable)
                .map(comentarioMapper::toResponse);
    }

    @Override
    @Transactional
    public TicketComentarioResponse create(TicketComentarioRequest request) {
        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", request.getTicketId()));

        Long personaId = 1L; // Fallback
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", personaId));

        auditInterceptor.setUsuarioActual();
        TicketComentario comentario = comentarioMapper.toEntity(request);
        comentario.setTicket(ticket);
        comentario.setPersona(persona);
        
        comentario = comentarioRepository.save(comentario);
        
        eventPublisher.publishEvent(new TicketEvent(this, ticket, TicketEvent.EventType.NUEVO_COMENTARIO, 
                "Nuevo comentario añadido por " + persona.getNombres()));
        
        log.info("Comentario creado en ticket: ticketId={}, comentarioId={}", ticket.getId(), comentario.getId());
        return comentarioMapper.toResponse(comentario);
    }

    @Override
    public void delete(Long id) {
        TicketComentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TicketComentario", "id", id));

        auditInterceptor.setUsuarioActual();
        comentarioRepository.delete(comentario);
        
        log.info("Comentario eliminado: id={}", id);
    }
}
