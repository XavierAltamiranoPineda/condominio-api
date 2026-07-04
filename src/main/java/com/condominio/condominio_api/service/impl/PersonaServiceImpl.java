package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.PersonaRequest;
import com.condominio.condominio_api.dto.response.PersonaResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.PersonaMapper;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.service.interfaces.PersonaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public PersonaResponse findById(Long id) {
        return personaRepository.findById(id)
                .map(personaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));
    }

    @Override
    public Page<PersonaResponse> findAll(Pageable pageable) {
        return personaRepository.findAll(pageable)
                .map(personaMapper::toResponse);
    }

    @Override
    public PersonaResponse create(PersonaRequest request) {
        if (personaRepository.existsByTipoIdentificacionAndNumeroIdentificacionIgnoreCase(request.getTipoIdentificacion(), request.getNumeroIdentificacion())) {
            throw new ResourceAlreadyExistsException("Persona", "identificación", request.getTipoIdentificacion() + "-" + request.getNumeroIdentificacion());
        }

        if (personaRepository.existsByCorreoIgnoreCase(request.getCorreo())) {
            throw new ResourceAlreadyExistsException("Persona", "correo", request.getCorreo());
        }

        auditInterceptor.setUsuarioActual();
        Persona persona = personaMapper.toEntity(request);
        persona = personaRepository.save(persona);

        log.info("Persona creada: id={}, correo={}", persona.getId(), persona.getCorreo());
        return personaMapper.toResponse(persona);
    }

    @Override
    public PersonaResponse update(Long id, PersonaRequest request) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));

        if (personaRepository.existsByTipoIdentificacionAndNumeroIdentificacionIgnoreCaseAndIdNot(request.getTipoIdentificacion(), request.getNumeroIdentificacion(), id)) {
            throw new ResourceAlreadyExistsException("Persona", "identificación", request.getTipoIdentificacion() + "-" + request.getNumeroIdentificacion());
        }

        if (personaRepository.existsByCorreoIgnoreCaseAndIdNot(request.getCorreo(), id)) {
            throw new ResourceAlreadyExistsException("Persona", "correo", request.getCorreo());
        }

        auditInterceptor.setUsuarioActual();
        personaMapper.updateEntityFromRequest(request, persona);
        persona = personaRepository.save(persona);

        log.info("Persona actualizada: id={}, correo={}", persona.getId(), persona.getCorreo());
        return personaMapper.toResponse(persona);
    }

    @Override
    public void delete(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));

        auditInterceptor.setUsuarioActual();
        personaRepository.delete(persona);

        log.info("Persona eliminada: id={}", id);
    }
}
