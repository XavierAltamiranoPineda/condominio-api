package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.ConvenioPagoRequest;
import com.condominio.condominio_api.dto.response.ConvenioPagoResponse;
import com.condominio.condominio_api.entity.ConvenioPago;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.ConvenioPagoMapper;
import com.condominio.condominio_api.repository.ConvenioPagoRepository;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.repository.UnidadRepository;
import com.condominio.condominio_api.service.interfaces.ConvenioPagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConvenioPagoServiceImpl implements ConvenioPagoService {

    private final ConvenioPagoRepository convenioRepository;
    private final PersonaRepository personaRepository;
    private final UnidadRepository unidadRepository;
    private final ConvenioPagoMapper convenioMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public ConvenioPagoResponse findById(Long id) {
        return convenioRepository.findByIdWithDetails(id)
                .map(convenioMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("ConvenioPago", "id", id));
    }

    @Override
    public Page<ConvenioPagoResponse> findByCondominioId(Long condominioId, Pageable pageable) {
        return convenioRepository.findByCondominioIdWithDetails(condominioId, pageable)
                .map(convenioMapper::toResponse);
    }

    @Override
    public Page<ConvenioPagoResponse> findByPersonaId(Long personaId, Pageable pageable) {
        return convenioRepository.findByPersonaIdWithDetails(personaId, pageable)
                .map(convenioMapper::toResponse);
    }

    @Override
    @Transactional
    public ConvenioPagoResponse create(ConvenioPagoRequest request) {
        Persona persona = personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));

        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));

        auditInterceptor.setUsuarioActual();
        ConvenioPago convenio = convenioMapper.toEntity(request);
        convenio.setPersona(persona);
        convenio.setUnidad(unidad);
        convenio.setEstado(ConvenioPago.EstadoConvenio.ACTIVO);
        
        convenio = convenioRepository.save(convenio);
        log.info("Convenio de pago creado: id={}, monto={}", convenio.getId(), convenio.getMontoTotal());
        return convenioMapper.toResponse(convenio);
    }

    @Override
    @Transactional
    public ConvenioPagoResponse update(Long id, ConvenioPagoRequest request) {
        ConvenioPago convenio = convenioRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("ConvenioPago", "id", id));

        if (convenio.getEstado() == ConvenioPago.EstadoConvenio.COMPLETADO || convenio.getEstado() == ConvenioPago.EstadoConvenio.ANULADO) {
            throw new BusinessException("No se puede editar un convenio COMPLETADO o ANULADO");
        }

        auditInterceptor.setUsuarioActual();
        convenioMapper.updateEntityFromRequest(request, convenio);

        if (request.getPersonaId() != null && !convenio.getPersona().getId().equals(request.getPersonaId())) {
            Persona persona = personaRepository.findById(request.getPersonaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", request.getPersonaId()));
            convenio.setPersona(persona);
        }

        if (request.getUnidadId() != null && !convenio.getUnidad().getId().equals(request.getUnidadId())) {
            Unidad unidad = unidadRepository.findById(request.getUnidadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));
            convenio.setUnidad(unidad);
        }

        convenio = convenioRepository.save(convenio);
        log.info("Convenio de pago actualizado: id={}", convenio.getId());
        return convenioMapper.toResponse(convenio);
    }

    @Override
    @Transactional
    public ConvenioPagoResponse cambiarEstado(Long id, ConvenioPago.EstadoConvenio nuevoEstado) {
        ConvenioPago convenio = convenioRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("ConvenioPago", "id", id));

        auditInterceptor.setUsuarioActual();
        convenio.setEstado(nuevoEstado);
        
        convenio = convenioRepository.save(convenio);
        log.info("Estado de convenio cambiado: id={}, nuevoEstado={}", convenio.getId(), nuevoEstado);
        return convenioMapper.toResponse(convenio);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ConvenioPago convenio = convenioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ConvenioPago", "id", id));
        
        if (convenio.getEstado() != ConvenioPago.EstadoConvenio.ACTIVO) {
            throw new BusinessException("Solo se pueden eliminar convenios en estado ACTIVO");
        }

        auditInterceptor.setUsuarioActual();
        convenioRepository.delete(convenio);
        log.info("Convenio de pago eliminado: id={}", id);
    }
}
