package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.ActaRequest;
import com.condominio.condominio_api.dto.response.ActaResponse;
import com.condominio.condominio_api.entity.Acta;
import com.condominio.condominio_api.entity.Archivo;
import com.condominio.condominio_api.entity.Asamblea;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.ActaMapper;
import com.condominio.condominio_api.repository.ActaRepository;
import com.condominio.condominio_api.repository.ArchivoRepository;
import com.condominio.condominio_api.repository.AsambleaRepository;
import com.condominio.condominio_api.service.interfaces.ActaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActaServiceImpl implements ActaService {

    private final ActaRepository actaRepository;
    private final AsambleaRepository asambleaRepository;
    private final ArchivoRepository archivoRepository;
    private final ActaMapper actaMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public ActaResponse findById(Long id) {
        return actaRepository.findById(id)
                .map(actaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Acta", "id", id));
    }

    @Override
    public ActaResponse findByAsambleaId(Long asambleaId) {
        return actaRepository.findByAsambleaIdWithArchivos(asambleaId)
                .map(actaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Acta", "asambleaId", asambleaId));
    }

    @Override
    @Transactional
    public ActaResponse create(ActaRequest request) {
        Asamblea asamblea = asambleaRepository.findById(request.getAsambleaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asamblea", "id", request.getAsambleaId()));

        if (asamblea.getEstado() != Asamblea.EstadoAsamblea.FINALIZADA) {
            throw new BusinessException("Solo se pueden crear actas para asambleas FINALIZADAS");
        }

        Optional<Acta> existente = actaRepository.findByAsambleaIdWithArchivos(asamblea.getId());
        if (existente.isPresent()) {
            throw new BusinessException("Esta asamblea ya tiene un acta registrada");
        }

        List<Archivo> archivos = new ArrayList<>();
        if (request.getArchivosIds() != null && !request.getArchivosIds().isEmpty()) {
            archivos = archivoRepository.findAllById(request.getArchivosIds());
        }

        auditInterceptor.setUsuarioActual();
        Acta acta = actaMapper.toEntity(request);
        acta.setAsamblea(asamblea);
        acta.setArchivos(archivos);
        
        acta = actaRepository.save(acta);
        log.info("Acta creada: id={}, asambleaId={}", acta.getId(), asamblea.getId());
        return actaMapper.toResponse(acta);
    }

    @Override
    @Transactional
    public ActaResponse update(Long id, ActaRequest request) {
        Acta acta = actaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acta", "id", id));

        List<Archivo> archivos = acta.getArchivos();
        if (request.getArchivosIds() != null) {
            archivos = archivoRepository.findAllById(request.getArchivosIds());
        }

        auditInterceptor.setUsuarioActual();
        actaMapper.updateEntityFromRequest(request, acta);
        acta.setArchivos(archivos);
        
        acta = actaRepository.save(acta);
        log.info("Acta actualizada: id={}", acta.getId());
        return actaMapper.toResponse(acta);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Acta acta = actaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acta", "id", id));
        
        auditInterceptor.setUsuarioActual();
        actaRepository.delete(acta);
        log.info("Acta eliminada: id={}", id);
    }
}
