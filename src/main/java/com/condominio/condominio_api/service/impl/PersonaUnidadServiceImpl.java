package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.PersonaUnidadRequest;
import com.condominio.condominio_api.dto.response.PersonaUnidadResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.PersonaUnidad;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.PersonaUnidadMapper;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.repository.PersonaUnidadRepository;
import com.condominio.condominio_api.repository.UnidadRepository;
import com.condominio.condominio_api.service.interfaces.PersonaUnidadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonaUnidadServiceImpl implements PersonaUnidadService {

    private final PersonaUnidadRepository personaUnidadRepository;
    private final PersonaRepository personaRepository;
    private final UnidadRepository unidadRepository;
    private final PersonaUnidadMapper personaUnidadMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public PersonaUnidadResponse findById(Long id) {
        return personaUnidadRepository.findByIdWithDetails(id)
                .map(personaUnidadMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("PersonaUnidad", "id", id));
    }

    @Override
    public Page<PersonaUnidadResponse> findAll(Pageable pageable) {
        return personaUnidadRepository.findAllWithDetails(pageable)
                .map(personaUnidadMapper::toResponse);
    }

    @Override
    public Page<PersonaUnidadResponse> findByPersonaId(Long personaId, Pageable pageable) {
        if (!personaRepository.existsById(personaId)) {
            throw new ResourceNotFoundException("Persona", "id", personaId);
        }
        return personaUnidadRepository.findByPersonaIdWithDetails(personaId, pageable)
                .map(personaUnidadMapper::toResponse);
    }

    @Override
    public Page<PersonaUnidadResponse> findByUnidadId(Long unidadId, Pageable pageable) {
        if (!unidadRepository.existsById(unidadId)) {
            throw new ResourceNotFoundException("Unidad", "id", unidadId);
        }
        return personaUnidadRepository.findByUnidadIdWithDetails(unidadId, pageable)
                .map(personaUnidadMapper::toResponse);
    }

    @Override
    public PersonaUnidadResponse create(PersonaUnidadRequest request) {
        Persona persona = personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));

        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));

        if (request.getFechaFin() != null && request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new BusinessException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        if (personaUnidadRepository.existsByPersonaIdAndUnidadIdAndEstado(request.getPersonaId(), request.getUnidadId(), request.getEstado())) {
            throw new ResourceAlreadyExistsException("PersonaUnidad", "personaId+unidadId+estado", request.getPersonaId() + "-" + request.getUnidadId() + "-" + request.getEstado());
        }

        auditInterceptor.setUsuarioActual();
        PersonaUnidad personaUnidad = personaUnidadMapper.toEntity(request);
        personaUnidad.setPersona(persona);
        personaUnidad.setUnidad(unidad);
        personaUnidad = personaUnidadRepository.save(personaUnidad);

        log.info("PersonaUnidad creada: id={}", personaUnidad.getId());
        return personaUnidadMapper.toResponse(personaUnidad);
    }

    @Override
    public PersonaUnidadResponse update(Long id, PersonaUnidadRequest request) {
        PersonaUnidad personaUnidad = personaUnidadRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("PersonaUnidad", "id", id));

        if (!personaUnidad.getPersona().getId().equals(request.getPersonaId())) {
            Persona persona = personaRepository.findById(request.getPersonaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));
            personaUnidad.setPersona(persona);
        }

        if (!personaUnidad.getUnidad().getId().equals(request.getUnidadId())) {
            Unidad unidad = unidadRepository.findById(request.getUnidadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));
            personaUnidad.setUnidad(unidad);
        }

        if (request.getFechaFin() != null && request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new BusinessException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        auditInterceptor.setUsuarioActual();
        personaUnidadMapper.updateEntityFromRequest(request, personaUnidad);
        personaUnidad = personaUnidadRepository.save(personaUnidad);

        log.info("PersonaUnidad actualizada: id={}", personaUnidad.getId());
        return personaUnidadMapper.toResponse(personaUnidad);
    }

    @Override
    public void delete(Long id) {
        PersonaUnidad personaUnidad = personaUnidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PersonaUnidad", "id", id));

        auditInterceptor.setUsuarioActual();
        personaUnidadRepository.delete(personaUnidad);

        log.info("PersonaUnidad eliminada: id={}", id);
    }
}
