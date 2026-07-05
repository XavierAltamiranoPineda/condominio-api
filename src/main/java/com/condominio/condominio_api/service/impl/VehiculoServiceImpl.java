package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.dto.request.VehiculoRequest;
import com.condominio.condominio_api.dto.response.VehiculoResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.entity.Vehiculo;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.VehiculoMapper;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.repository.UnidadRepository;
import com.condominio.condominio_api.repository.VehiculoRepository;
import com.condominio.condominio_api.service.interfaces.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository repository;
    private final UnidadRepository unidadRepository;
    private final PersonaRepository personaRepository;
    private final VehiculoMapper mapper;

    @Override
    public VehiculoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo", "id", id));
    }

    @Override
    public List<VehiculoResponse> findByUnidadId(Long unidadId) {
        return mapper.toResponseList(repository.findByUnidadId(unidadId));
    }

    @Override
    public Page<VehiculoResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return repository.findByCondominioId(condominioId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public VehiculoResponse create(VehiculoRequest request) {
        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));
        
        Persona persona = null;
        if (request.getPersonaId() != null) {
            persona = personaRepository.findById(request.getPersonaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));
        }

        Vehiculo entity = mapper.toEntity(request);
        entity.setUnidad(unidad);
        entity.setPersonaActual(persona);
        
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public VehiculoResponse update(Long id, VehiculoRequest request) {
        Vehiculo entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo", "id", id));
        
        mapper.updateEntityFromRequest(request, entity);

        if (request.getUnidadId() != null && !entity.getUnidad().getId().equals(request.getUnidadId())) {
            Unidad unidad = unidadRepository.findById(request.getUnidadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));
            entity.setUnidad(unidad);
        }

        if (request.getPersonaId() != null && (entity.getPersonaActual() == null || !entity.getPersonaActual().getId().equals(request.getPersonaId()))) {
            Persona persona = personaRepository.findById(request.getPersonaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));
            entity.setPersonaActual(persona);
        } else if (request.getPersonaId() == null) {
            entity.setPersonaActual(null);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Vehiculo entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo", "id", id));
        repository.delete(entity);
    }
}
