package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.VisitanteRequest;
import com.condominio.condominio_api.dto.response.VisitanteResponse;
import com.condominio.condominio_api.entity.Visitante;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.VisitanteMapper;
import com.condominio.condominio_api.repository.VisitanteRepository;
import com.condominio.condominio_api.service.interfaces.VisitanteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitanteServiceImpl implements VisitanteService {

    private final VisitanteRepository visitanteRepository;
    private final VisitanteMapper visitanteMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public VisitanteResponse findById(Long id) {
        return visitanteRepository.findById(id)
                .map(visitanteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Visitante", "id", id));
    }

    @Override
    public VisitanteResponse findByCedula(String cedula) {
        return visitanteRepository.findByCedula(cedula)
                .map(visitanteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Visitante", "cedula", cedula));
    }

    @Override
    public Page<VisitanteResponse> findAll(Pageable pageable) {
        return visitanteRepository.findAll(pageable)
                .map(visitanteMapper::toResponse);
    }

    @Override
    @Transactional
    public VisitanteResponse create(VisitanteRequest request) {
        if (request.getCedula() != null) {
            Optional<Visitante> existente = visitanteRepository.findByCedula(request.getCedula());
            if (existente.isPresent()) {
                throw new BusinessException("Ya existe un visitante con la cédula " + request.getCedula());
            }
        }

        auditInterceptor.setUsuarioActual();
        Visitante visitante = visitanteMapper.toEntity(request);
        visitante = visitanteRepository.save(visitante);
        log.info("Visitante creado: id={}, nombre={}", visitante.getId(), visitante.getNombre());
        return visitanteMapper.toResponse(visitante);
    }

    @Override
    @Transactional
    public VisitanteResponse update(Long id, VisitanteRequest request) {
        Visitante visitante = visitanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visitante", "id", id));

        if (request.getCedula() != null && !request.getCedula().equals(visitante.getCedula())) {
            Optional<Visitante> existente = visitanteRepository.findByCedula(request.getCedula());
            if (existente.isPresent() && !existente.get().getId().equals(id)) {
                throw new BusinessException("Ya existe otro visitante con la cédula " + request.getCedula());
            }
        }

        auditInterceptor.setUsuarioActual();
        visitanteMapper.updateEntityFromRequest(request, visitante);
        visitante = visitanteRepository.save(visitante);
        log.info("Visitante actualizado: id={}", visitante.getId());
        return visitanteMapper.toResponse(visitante);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Visitante visitante = visitanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visitante", "id", id));
        
        auditInterceptor.setUsuarioActual();
        visitanteRepository.delete(visitante);
        log.info("Visitante eliminado: id={}", id);
    }
}
