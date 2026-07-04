package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.CondominioRequest;
import com.condominio.condominio_api.dto.response.CondominioResponse;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.CondominioMapper;
import com.condominio.condominio_api.repository.CondominioRepository;
import com.condominio.condominio_api.service.interfaces.CondominioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CondominioServiceImpl implements CondominioService {

    private final CondominioRepository condominioRepository;
    private final CondominioMapper condominioMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public CondominioResponse findById(Long id) {
        return condominioRepository.findById(id)
                .map(condominioMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", id));
    }

    @Override
    public Page<CondominioResponse> findAll(Pageable pageable) {
        return condominioRepository.findAll(pageable)
                .map(condominioMapper::toResponse);
    }

    @Override
    public CondominioResponse create(CondominioRequest request) {
        if (condominioRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ResourceAlreadyExistsException("Condominio", "nombre", request.getNombre());
        }

        auditInterceptor.setUsuarioActual();
        Condominio condominio = condominioMapper.toEntity(request);
        condominio = condominioRepository.save(condominio);

        log.info("Condominio creado: id={}, nombre={}", condominio.getId(), condominio.getNombre());
        return condominioMapper.toResponse(condominio);
    }

    @Override
    public CondominioResponse update(Long id, CondominioRequest request) {
        Condominio condominio = condominioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", id));

        if (condominioRepository.existsByNombreIgnoreCaseAndIdNot(request.getNombre(), id)) {
            throw new ResourceAlreadyExistsException("Condominio", "nombre", request.getNombre());
        }

        auditInterceptor.setUsuarioActual();
        condominioMapper.updateEntityFromRequest(request, condominio);
        condominio = condominioRepository.save(condominio);

        log.info("Condominio actualizado: id={}, nombre={}", condominio.getId(), condominio.getNombre());
        return condominioMapper.toResponse(condominio);
    }

    @Override
    public void delete(Long id) {
        Condominio condominio = condominioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", id));

        auditInterceptor.setUsuarioActual();
        condominioRepository.delete(condominio);

        log.info("Condominio eliminado: id={}", id);
    }
}
