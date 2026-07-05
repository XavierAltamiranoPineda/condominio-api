package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.dto.request.ParqueaderoRequest;
import com.condominio.condominio_api.dto.response.ParqueaderoResponse;
import com.condominio.condominio_api.entity.Parqueadero;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.ParqueaderoMapper;
import com.condominio.condominio_api.repository.ParqueaderoRepository;
import com.condominio.condominio_api.repository.UnidadRepository;
import com.condominio.condominio_api.service.interfaces.ParqueaderoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParqueaderoServiceImpl implements ParqueaderoService {

    private final ParqueaderoRepository repository;
    private final UnidadRepository unidadRepository;
    private final ParqueaderoMapper mapper;

    @Override
    public ParqueaderoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Parqueadero", "id", id));
    }

    @Override
    public List<ParqueaderoResponse> findByUnidadId(Long unidadId) {
        return mapper.toResponseList(repository.findByUnidadId(unidadId));
    }

    @Override
    public Page<ParqueaderoResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return repository.findByCondominioId(condominioId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public ParqueaderoResponse create(ParqueaderoRequest request) {
        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));
        
        Parqueadero entity = mapper.toEntity(request);
        entity.setUnidad(unidad);
        entity.setEstado(Parqueadero.EstadoParqueadero.DISPONIBLE);
        
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public ParqueaderoResponse update(Long id, ParqueaderoRequest request) {
        Parqueadero entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parqueadero", "id", id));
        
        mapper.updateEntityFromRequest(request, entity);

        if (request.getUnidadId() != null && !entity.getUnidad().getId().equals(request.getUnidadId())) {
            Unidad unidad = unidadRepository.findById(request.getUnidadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));
            entity.setUnidad(unidad);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public ParqueaderoResponse cambiarEstado(Long id, Parqueadero.EstadoParqueadero estado) {
        Parqueadero entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parqueadero", "id", id));
        entity.setEstado(estado);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Parqueadero entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parqueadero", "id", id));
        repository.delete(entity);
    }
}
