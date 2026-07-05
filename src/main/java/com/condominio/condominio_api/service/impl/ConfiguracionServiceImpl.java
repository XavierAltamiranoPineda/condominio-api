package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.dto.request.ConfiguracionRequest;
import com.condominio.condominio_api.dto.response.ConfiguracionResponse;
import com.condominio.condominio_api.entity.Configuracion;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.ConfiguracionMapper;
import com.condominio.condominio_api.repository.ConfiguracionRepository;
import com.condominio.condominio_api.service.interfaces.ConfiguracionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final ConfiguracionRepository repository;
    private final ConfiguracionMapper mapper;

    @Override
    public ConfiguracionResponse findById(Long id) {
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", "id", id));
    }

    @Override
    public ConfiguracionResponse findByClave(String clave) {
        return repository.findByClave(clave).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", "clave", clave));
    }

    @Override
    public List<ConfiguracionResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional
    public ConfiguracionResponse create(ConfiguracionRequest request) {
        if (repository.findByClave(request.getClave()).isPresent()) {
            throw new BusinessException("Ya existe una configuración con la clave: " + request.getClave());
        }
        Configuracion entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public ConfiguracionResponse update(Long id, ConfiguracionRequest request) {
        Configuracion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", "id", id));
        mapper.updateEntityFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Configuracion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", "id", id));
        repository.delete(entity);
    }
}
