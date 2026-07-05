package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.VisitantePreautorizadoRequest;
import com.condominio.condominio_api.dto.response.VisitantePreautorizadoResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.entity.Visitante;
import com.condominio.condominio_api.entity.VisitantePreautorizado;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.VisitanteMapper;
import com.condominio.condominio_api.mapper.VisitantePreautorizadoMapper;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.repository.UnidadRepository;
import com.condominio.condominio_api.repository.VisitantePreautorizadoRepository;
import com.condominio.condominio_api.repository.VisitanteRepository;
import com.condominio.condominio_api.service.interfaces.VisitantePreautorizadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitantePreautorizadoServiceImpl implements VisitantePreautorizadoService {

    private final VisitantePreautorizadoRepository preautorizadoRepository;
    private final VisitanteRepository visitanteRepository;
    private final UnidadRepository unidadRepository;
    private final PersonaRepository personaRepository;
    private final VisitantePreautorizadoMapper preautorizadoMapper;
    private final VisitanteMapper visitanteMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public VisitantePreautorizadoResponse findById(Long id) {
        return preautorizadoRepository.findById(id)
                .map(preautorizadoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("VisitantePreautorizado", "id", id));
    }

    @Override
    public Page<VisitantePreautorizadoResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return preautorizadoRepository.findByCondominioIdWithDetails(condominioId, pageable)
                .map(preautorizadoMapper::toResponse);
    }

    @Override
    public Page<VisitantePreautorizadoResponse> findByUnidadId(Long unidadId, Pageable pageable) {
        return preautorizadoRepository.findByUnidadIdWithDetails(unidadId, pageable)
                .map(preautorizadoMapper::toResponse);
    }

    @Override
    @Transactional
    public VisitantePreautorizadoResponse create(VisitantePreautorizadoRequest request) {
        Visitante visitante;
        
        if (request.getVisitanteId() != null) {
            visitante = visitanteRepository.findById(request.getVisitanteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Visitante", "id", request.getVisitanteId()));
        } else if (request.getVisitanteNuevo() != null) {
            if (request.getVisitanteNuevo().getCedula() != null && visitanteRepository.findByCedula(request.getVisitanteNuevo().getCedula()).isPresent()) {
                throw new BusinessException("Ya existe un visitante con esa cédula. Usa visitanteId.");
            }
            visitante = visitanteMapper.toEntity(request.getVisitanteNuevo());
            visitante = visitanteRepository.save(visitante);
        } else {
            throw new BusinessException("Debe proveer visitanteId o visitanteNuevo");
        }

        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));

        Persona autorizador = personaRepository.findById(request.getAutorizadoPorId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona (Autorizador)", "id", request.getAutorizadoPorId()));

        auditInterceptor.setUsuarioActual();
        VisitantePreautorizado preautorizado = preautorizadoMapper.toEntity(request);
        preautorizado.setVisitante(visitante);
        preautorizado.setUnidad(unidad);
        preautorizado.setAutorizadoPor(autorizador);
        
        preautorizado = preautorizadoRepository.save(preautorizado);
        log.info("Visitante preautorizado: visitanteId={}, unidadId={}", visitante.getId(), unidad.getId());
        return preautorizadoMapper.toResponse(preautorizado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        VisitantePreautorizado preautorizado = preautorizadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VisitantePreautorizado", "id", id));
        
        auditInterceptor.setUsuarioActual();
        preautorizadoRepository.delete(preautorizado);
        log.info("Preautorización eliminada: id={}", id);
    }
}
