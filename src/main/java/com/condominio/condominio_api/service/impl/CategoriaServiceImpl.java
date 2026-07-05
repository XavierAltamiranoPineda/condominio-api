package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.dto.request.CategoriaRequest;
import com.condominio.condominio_api.dto.response.CategoriaResponse;
import com.condominio.condominio_api.entity.Categoria;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.CategoriaMapper;
import com.condominio.condominio_api.repository.CategoriaRepository;
import com.condominio.condominio_api.service.interfaces.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    @Override
    public CategoriaResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
    }

    @Override
    public List<CategoriaResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional
    public CategoriaResponse create(CategoriaRequest request) {
        if (repository.findByNombre(request.getNombre()).isPresent()) {
            throw new BusinessException("Ya existe una categoría con el nombre: " + request.getNombre());
        }
        Categoria entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public CategoriaResponse update(Long id, CategoriaRequest request) {
        Categoria entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Categoria entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
        repository.delete(entity);
    }
}
