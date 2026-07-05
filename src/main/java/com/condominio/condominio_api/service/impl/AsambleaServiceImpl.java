package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.AsambleaRequest;
import com.condominio.condominio_api.dto.response.AsambleaResponse;
import com.condominio.condominio_api.entity.Asamblea;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.AsambleaMapper;
import com.condominio.condominio_api.repository.AsambleaRepository;
import com.condominio.condominio_api.repository.CondominioRepository;
import com.condominio.condominio_api.service.interfaces.AsambleaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsambleaServiceImpl implements AsambleaService {

    private final AsambleaRepository asambleaRepository;
    private final CondominioRepository condominioRepository;
    private final AsambleaMapper asambleaMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public AsambleaResponse findById(Long id) {
        return asambleaRepository.findById(id)
                .map(asambleaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Asamblea", "id", id));
    }

    @Override
    public Page<AsambleaResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return asambleaRepository.findByCondominioId(condominioId, pageable)
                .map(asambleaMapper::toResponse);
    }

    @Override
    @Transactional
    public AsambleaResponse create(AsambleaRequest request) {
        Condominio condominio = condominioRepository.findById(request.getCondominioId())
                .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", request.getCondominioId()));

        auditInterceptor.setUsuarioActual();
        Asamblea asamblea = asambleaMapper.toEntity(request);
        asamblea.setCondominio(condominio);
        asamblea.setEstado(Asamblea.EstadoAsamblea.PROGRAMADA);
        
        asamblea = asambleaRepository.save(asamblea);
        log.info("Asamblea creada: id={}, fecha={}", asamblea.getId(), asamblea.getFecha());
        return asambleaMapper.toResponse(asamblea);
    }

    @Override
    @Transactional
    public AsambleaResponse update(Long id, AsambleaRequest request) {
        Asamblea asamblea = asambleaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asamblea", "id", id));

        if (asamblea.getEstado() == Asamblea.EstadoAsamblea.FINALIZADA || asamblea.getEstado() == Asamblea.EstadoAsamblea.CANCELADA) {
            throw new BusinessException("No se puede editar una asamblea finalizada o cancelada");
        }

        auditInterceptor.setUsuarioActual();
        asambleaMapper.updateEntityFromRequest(request, asamblea);
        
        if (request.getCondominioId() != null && !asamblea.getCondominio().getId().equals(request.getCondominioId())) {
            Condominio condominio = condominioRepository.findById(request.getCondominioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", request.getCondominioId()));
            asamblea.setCondominio(condominio);
        }

        asamblea = asambleaRepository.save(asamblea);
        log.info("Asamblea actualizada: id={}", asamblea.getId());
        return asambleaMapper.toResponse(asamblea);
    }

    @Override
    @Transactional
    public AsambleaResponse cambiarEstado(Long id, Asamblea.EstadoAsamblea nuevoEstado) {
        Asamblea asamblea = asambleaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asamblea", "id", id));

        auditInterceptor.setUsuarioActual();
        asamblea.setEstado(nuevoEstado);
        
        asamblea = asambleaRepository.save(asamblea);
        log.info("Estado de asamblea cambiado: id={}, nuevoEstado={}", asamblea.getId(), nuevoEstado);
        return asambleaMapper.toResponse(asamblea);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Asamblea asamblea = asambleaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asamblea", "id", id));
        
        if (asamblea.getEstado() != Asamblea.EstadoAsamblea.PROGRAMADA) {
            throw new BusinessException("Solo se pueden eliminar asambleas que estén PROGRAMADAS");
        }

        auditInterceptor.setUsuarioActual();
        asambleaRepository.delete(asamblea);
        log.info("Asamblea eliminada: id={}", id);
    }
}
