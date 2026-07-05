package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.dto.request.NotificacionRequest;
import com.condominio.condominio_api.dto.response.NotificacionResponse;
import com.condominio.condominio_api.entity.Notificacion;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.NotificacionMapper;
import com.condominio.condominio_api.repository.NotificacionRepository;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.service.interfaces.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository repository;
    private final PersonaRepository personaRepository;
    private final NotificacionMapper mapper;

    @Override
    public NotificacionResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Notificacion", "id", id));
    }

    @Override
    public Page<NotificacionResponse> findByPersonaId(Long personaId, Pageable pageable) {
        return repository.findByPersonaId(personaId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public NotificacionResponse create(NotificacionRequest request) {
        Persona persona = personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));

        Notificacion entity = mapper.toEntity(request);
        entity.setPersona(persona);
        entity.setEstadoEnvio(Notificacion.EstadoEnvio.PENDIENTE);
        entity.setLeido(false);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public NotificacionResponse marcarComoEnviada(Long id) {
        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificacion", "id", id));
        
        entity.setEstadoEnvio(Notificacion.EstadoEnvio.ENVIADO);
        entity.setFechaEnvio(OffsetDateTime.now());
        
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public NotificacionResponse marcarComoLeida(Long id) {
        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificacion", "id", id));
        
        entity.setLeido(true);
        entity.setFechaLectura(OffsetDateTime.now());
        
        return mapper.toResponse(repository.save(entity));
    }
}
