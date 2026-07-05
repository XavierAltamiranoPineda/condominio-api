package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.MultaRequest;
import com.condominio.condominio_api.dto.response.MultaResponse;
import com.condominio.condominio_api.entity.Cuota;
import com.condominio.condominio_api.entity.Multa;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.MultaMapper;
import com.condominio.condominio_api.repository.CuotaRepository;
import com.condominio.condominio_api.repository.MultaRepository;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.repository.UnidadRepository;
import com.condominio.condominio_api.service.interfaces.MultaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultaServiceImpl implements MultaService {

    private final MultaRepository multaRepository;
    private final UnidadRepository unidadRepository;
    private final PersonaRepository personaRepository;
    private final CuotaRepository cuotaRepository;
    private final MultaMapper multaMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public MultaResponse findById(Long id) {
        return multaRepository.findByIdWithDetails(id)
                .map(multaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Multa", "id", id));
    }

    @Override
    public Page<MultaResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return multaRepository.findByCondominioIdWithDetails(condominioId, pageable)
                .map(multaMapper::toResponse);
    }

    @Override
    public Page<MultaResponse> findByPersonaId(Long personaId, Pageable pageable) {
        return multaRepository.findByPersonaIdWithDetails(personaId, pageable)
                .map(multaMapper::toResponse);
    }

    @Override
    @Transactional
    public MultaResponse create(MultaRequest request) {
        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));

        Persona persona = personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));

        Cuota cuota = null;
        if (request.getCuotaId() != null) {
            cuota = cuotaRepository.findById(request.getCuotaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuota", "id", request.getCuotaId()));
        }

        auditInterceptor.setUsuarioActual();
        Multa multa = multaMapper.toEntity(request);
        multa.setUnidad(unidad);
        multa.setPersona(persona);
        multa.setCuota(cuota);
        multa.setEstado(Multa.EstadoMulta.REGISTRADA);
        
        multa = multaRepository.save(multa);
        log.info("Multa creada: id={}, valor={}", multa.getId(), multa.getValor());
        return multaMapper.toResponse(multa);
    }

    @Override
    @Transactional
    public MultaResponse update(Long id, MultaRequest request) {
        Multa multa = multaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multa", "id", id));

        if (multa.getEstado() == Multa.EstadoMulta.FACTURADA || multa.getEstado() == Multa.EstadoMulta.ANULADA) {
            throw new BusinessException("No se puede editar una multa FACTURADA o ANULADA");
        }

        auditInterceptor.setUsuarioActual();
        multaMapper.updateEntityFromRequest(request, multa);

        if (request.getUnidadId() != null && !multa.getUnidad().getId().equals(request.getUnidadId())) {
            Unidad unidad = unidadRepository.findById(request.getUnidadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));
            multa.setUnidad(unidad);
        }

        if (request.getPersonaId() != null && !multa.getPersona().getId().equals(request.getPersonaId())) {
            Persona persona = personaRepository.findById(request.getPersonaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));
            multa.setPersona(persona);
        }

        if (request.getCuotaId() != null && (multa.getCuota() == null || !multa.getCuota().getId().equals(request.getCuotaId()))) {
            Cuota cuota = cuotaRepository.findById(request.getCuotaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuota", "id", request.getCuotaId()));
            multa.setCuota(cuota);
        }

        multa = multaRepository.save(multa);
        log.info("Multa actualizada: id={}", multa.getId());
        return multaMapper.toResponse(multa);
    }

    @Override
    @Transactional
    public MultaResponse cambiarEstado(Long id, Multa.EstadoMulta nuevoEstado) {
        Multa multa = multaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multa", "id", id));

        auditInterceptor.setUsuarioActual();
        multa.setEstado(nuevoEstado);
        
        multa = multaRepository.save(multa);
        log.info("Estado de multa cambiado: id={}, nuevoEstado={}", multa.getId(), nuevoEstado);
        return multaMapper.toResponse(multa);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multa", "id", id));
        
        if (multa.getEstado() != Multa.EstadoMulta.REGISTRADA) {
            throw new BusinessException("Solo se pueden eliminar multas en estado REGISTRADA. Si ya fue facturada, use anulación.");
        }

        auditInterceptor.setUsuarioActual();
        multaRepository.delete(multa);
        log.info("Multa eliminada: id={}", id);
    }
}
