package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.UnidadRequest;
import com.condominio.condominio_api.dto.response.UnidadResponse;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.entity.EstadoUnidad;
import com.condominio.condominio_api.entity.Torre;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.entity.enums.TipoUnidadEnum;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.UnidadMapper;
import com.condominio.condominio_api.repository.CondominioRepository;
import com.condominio.condominio_api.repository.EstadoUnidadRepository;
import com.condominio.condominio_api.repository.TorreRepository;
import com.condominio.condominio_api.repository.UnidadRepository;
import com.condominio.condominio_api.service.interfaces.UnidadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnidadServiceImpl implements UnidadService {

    private final UnidadRepository unidadRepository;
    private final CondominioRepository condominioRepository;
    private final TorreRepository torreRepository;
    private final EstadoUnidadRepository estadoUnidadRepository;
    private final UnidadMapper unidadMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public UnidadResponse findById(Long id) {
        return unidadRepository.findByIdWithDetails(id)
                .map(unidadMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", id));
    }

    @Override
    public Page<UnidadResponse> findAll(Pageable pageable) {
        return unidadRepository.findAllWithDetails(pageable)
                .map(unidadMapper::toResponse);
    }

    @Override
    public Page<UnidadResponse> findByEstado(String estado, Pageable pageable) {
        return unidadRepository.findByEstadoWithDetails(estado, pageable)
                .map(unidadMapper::toResponse);
    }

    @Override
    public Page<UnidadResponse> findByTipo(TipoUnidadEnum tipo, Pageable pageable) {
        return unidadRepository.findByTipoWithDetails(tipo, pageable)
                .map(unidadMapper::toResponse);
    }

    @Override
    public Page<UnidadResponse> findByTorreId(Long torreId, Pageable pageable) {
        return unidadRepository.findByTorreIdWithDetails(torreId, pageable)
                .map(unidadMapper::toResponse);
    }

    @Override
    public UnidadResponse create(UnidadRequest request) {
        Condominio condominio = condominioRepository.findById(request.getCondominioId())
                .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", request.getCondominioId()));

        Torre torre = null;
        if (request.getTorreId() != null) {
            torre = torreRepository.findById(request.getTorreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Torre", "id", request.getTorreId()));
            
            // Validar que la torre pertenezca al condominio
            if (!torre.getCondominio().getId().equals(condominio.getId())) {
                throw new BusinessException("La torre seleccionada no pertenece al condominio especificado.");
            }
        }

        EstadoUnidad estado = estadoUnidadRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("EstadoUnidad", "id", request.getEstadoId()));

        if (unidadRepository.existsByCondominioIdAndNumeroIgnoreCase(condominio.getId(), request.getNumero())) {
            throw new ResourceAlreadyExistsException("Unidad", "numero", request.getNumero());
        }

        auditInterceptor.setUsuarioActual();
        Unidad unidad = unidadMapper.toEntity(request);
        unidad.setCondominio(condominio);
        unidad.setTorre(torre);
        unidad.setEstado(estado);
        unidad = unidadRepository.save(unidad);

        log.info("Unidad creada: id={}, numero={}, condominioId={}", unidad.getId(), unidad.getNumero(), condominio.getId());
        return unidadMapper.toResponse(unidad);
    }

    @Override
    public UnidadResponse update(Long id, UnidadRequest request) {
        Unidad unidad = unidadRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", id));

        if (request.getCondominioId() != null && !unidad.getCondominio().getId().equals(request.getCondominioId())) {
            Condominio condominio = condominioRepository.findById(request.getCondominioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", request.getCondominioId()));
            unidad.setCondominio(condominio);
        }

        if (request.getTorreId() != null) {
            if (unidad.getTorre() == null || !unidad.getTorre().getId().equals(request.getTorreId())) {
                Torre torre = torreRepository.findById(request.getTorreId())
                        .orElseThrow(() -> new ResourceNotFoundException("Torre", "id", request.getTorreId()));
                
                if (!torre.getCondominio().getId().equals(unidad.getCondominio().getId())) {
                    throw new BusinessException("La torre seleccionada no pertenece al condominio especificado.");
                }
                unidad.setTorre(torre);
            }
        } else if (request.getTorreId() == null && request.getCondominioId() != null) {
             unidad.setTorre(null);
        }

        if (request.getEstadoId() != null && !unidad.getEstado().getId().equals(request.getEstadoId())) {
            EstadoUnidad estado = estadoUnidadRepository.findById(request.getEstadoId())
                    .orElseThrow(() -> new ResourceNotFoundException("EstadoUnidad", "id", request.getEstadoId()));
            unidad.setEstado(estado);
        }

        if (unidadRepository.existsByCondominioIdAndNumeroIgnoreCaseAndIdNot(unidad.getCondominio().getId(), request.getNumero(), id)) {
            throw new ResourceAlreadyExistsException("Unidad", "numero", request.getNumero());
        }

        auditInterceptor.setUsuarioActual();
        unidadMapper.updateEntityFromRequest(request, unidad);
        unidad = unidadRepository.save(unidad);

        log.info("Unidad actualizada: id={}, numero={}", unidad.getId(), unidad.getNumero());
        return unidadMapper.toResponse(unidad);
    }

    @Override
    public void delete(Long id) {
        Unidad unidad = unidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", id));

        auditInterceptor.setUsuarioActual();
        unidadRepository.delete(unidad);

        log.info("Unidad eliminada: id={}", id);
    }
}
