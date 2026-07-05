package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.AreaComunRequest;
import com.condominio.condominio_api.dto.response.AreaComunResponse;
import com.condominio.condominio_api.entity.AreaComun;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.AreaComunMapper;
import com.condominio.condominio_api.repository.AreaComunRepository;
import com.condominio.condominio_api.repository.CondominioRepository;
import com.condominio.condominio_api.service.interfaces.AreaComunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AreaComunServiceImpl implements AreaComunService {

    private final AreaComunRepository areaComunRepository;
    private final CondominioRepository condominioRepository;
    private final AreaComunMapper areaComunMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public AreaComunResponse findById(Long id) {
        return areaComunRepository.findById(id)
                .map(areaComunMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("AreaComun", "id", id));
    }

    @Override
    public Page<AreaComunResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return areaComunRepository.findByCondominioId(condominioId, pageable)
                .map(areaComunMapper::toResponse);
    }

    @Override
    @Transactional
    public AreaComunResponse create(AreaComunRequest request) {
        Condominio condominio = condominioRepository.findById(request.getCondominioId())
                .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", request.getCondominioId()));

        auditInterceptor.setUsuarioActual();
        AreaComun area = areaComunMapper.toEntity(request);
        area.setCondominio(condominio);
        
        area = areaComunRepository.save(area);
        log.info("AreaComun creada: id={}, nombre={}", area.getId(), area.getNombre());
        return areaComunMapper.toResponse(area);
    }

    @Override
    @Transactional
    public AreaComunResponse update(Long id, AreaComunRequest request) {
        AreaComun area = areaComunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AreaComun", "id", id));

        auditInterceptor.setUsuarioActual();
        areaComunMapper.updateEntityFromRequest(request, area);
        
        if (request.getCondominioId() != null && !area.getCondominio().getId().equals(request.getCondominioId())) {
            Condominio condominio = condominioRepository.findById(request.getCondominioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", request.getCondominioId()));
            area.setCondominio(condominio);
        }

        area = areaComunRepository.save(area);
        log.info("AreaComun actualizada: id={}", area.getId());
        return areaComunMapper.toResponse(area);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        AreaComun area = areaComunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AreaComun", "id", id));
        
        auditInterceptor.setUsuarioActual();
        areaComunRepository.delete(area);
        log.info("AreaComun eliminada: id={}", id);
    }
}
