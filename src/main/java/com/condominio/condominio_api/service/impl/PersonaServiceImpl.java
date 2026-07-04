package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.PersonaRequest;
import com.condominio.condominio_api.dto.response.PersonaResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.exception.BusinessException;
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
    public Page<PersonaResponse> findAll(Pageable pageable) {
        return personaRepository.findAll(pageable)
                .map(personaMapper::toResponse);
    }

    @Override
    public PersonaResponse findById(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));
        return personaMapper.toResponse(persona);
    }

    @Override
    public PersonaResponse create(PersonaRequest request) {
        if (personaRepository.existsByCorreo(request.getCorreo())) {
            throw new ResourceAlreadyExistsException("Persona", "correo", request.getCorreo());
        }
        if (personaRepository.existsByTipoIdentificacionAndNumeroIdentificacion(
                request.getTipoIdentificacion(), request.getNumeroIdentificacion())) {
            throw new ResourceAlreadyExistsException("Persona", "identificación",
                    request.getTipoIdentificacion() + ":" + request.getNumeroIdentificacion());
        }

        auditInterceptor.setUsuarioActual();
        Persona persona = personaMapper.toEntity(request);
        Persona saved = personaRepository.save(persona);
        log.info("Persona creada: id={}, identificación={}", saved.getId(), saved.getNumeroIdentificacion());
        return personaMapper.toResponse(saved);
    }

    @Override
    public PersonaResponse update(Long id, PersonaRequest request) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));

        if (!persona.getCorreo().equalsIgnoreCase(request.getCorreo())
                && personaRepository.existsByCorreo(request.getCorreo())) {
            throw new ResourceAlreadyExistsException("Persona", "correo", request.getCorreo());
        }

        if ((persona.getTipoIdentificacion() != request.getTipoIdentificacion()
                || !persona.getNumeroIdentificacion().equals(request.getNumeroIdentificacion()))
                && personaRepository.existsByTipoIdentificacionAndNumeroIdentificacion(
                        request.getTipoIdentificacion(), request.getNumeroIdentificacion())) {
            throw new ResourceAlreadyExistsException("Persona", "identificación",
                    request.getTipoIdentificacion() + ":" + request.getNumeroIdentificacion());
        }

        auditInterceptor.setUsuarioActual();
        personaMapper.updateFromRequest(request, persona);
        Persona saved = personaRepository.save(persona);
        log.info("Persona actualizada: id={}", saved.getId());
        return personaMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));

        auditInterceptor.setUsuarioActual();
        persona.setEstado(Persona.EstadoPersona.INACTIVO);
        personaRepository.save(persona);
        log.info("Persona desactivada lógicamente: id={}", id);
    }
}
