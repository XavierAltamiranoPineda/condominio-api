package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.TorreRequest;
import com.condominio.condominio_api.dto.response.TorreResponse;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.entity.Torre;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.TorreMapper;
import com.condominio.condominio_api.repository.CondominioRepository;
import com.condominio.condominio_api.repository.TorreRepository;
import com.condominio.condominio_api.service.interfaces.TorreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TorreServiceImpl implements TorreService {

    private final TorreRepository torreRepository;
    private final CondominioRepository condominioRepository;
    private final TorreMapper torreMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public TorreResponse findById(Long id) {
        return torreRepository.findByIdWithCondominio(id)
                .map(torreMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Torre", "id", id));
    }

    @Override
    public Page<TorreResponse> findAll(Pageable pageable) {
        return torreRepository.findAllWithCondominio(pageable)
                .map(torreMapper::toResponse);
    }

    @Override
    public TorreResponse create(TorreRequest request) {
        Condominio condominio = condominioRepository.findById(request.getCondominioId())
                .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", request.getCondominioId()));

        if (torreRepository.existsByNombreIgnoreCaseAndCondominioId(request.getNombre(), condominio.getId())) {
            throw new ResourceAlreadyExistsException("Torre", "nombre", request.getNombre());
        }

        auditInterceptor.setUsuarioActual();
        Torre torre = torreMapper.toEntity(request);
        torre.setCondominio(condominio);
        torre = torreRepository.save(torre);

        log.info("Torre creada: id={}, nombre={}, condominioId={}", torre.getId(), torre.getNombre(), condominio.getId());
        return torreMapper.toResponse(torre);
    }

    @Override
    public TorreResponse update(Long id, TorreRequest request) {
        Torre torre = torreRepository.findByIdWithCondominio(id)
                .orElseThrow(() -> new ResourceNotFoundException("Torre", "id", id));

        // Si se cambia de condominio, hay que validarlo
        if (request.getCondominioId() != null && !torre.getCondominio().getId().equals(request.getCondominioId())) {
            Condominio condominio = condominioRepository.findById(request.getCondominioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Condominio", "id", request.getCondominioId()));
            torre.setCondominio(condominio);
        }

        if (torreRepository.existsByNombreIgnoreCaseAndCondominioIdAndIdNot(request.getNombre(), torre.getCondominio().getId(), id)) {
            throw new ResourceAlreadyExistsException("Torre", "nombre", request.getNombre());
        }

        auditInterceptor.setUsuarioActual();
        torreMapper.updateEntityFromRequest(request, torre);
        torre = torreRepository.save(torre);

        log.info("Torre actualizada: id={}, nombre={}", torre.getId(), torre.getNombre());
        return torreMapper.toResponse(torre);
    }

    @Override
    public void delete(Long id) {
        Torre torre = torreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Torre", "id", id));

        auditInterceptor.setUsuarioActual();
        torreRepository.delete(torre);

        log.info("Torre eliminada: id={}", id);
    }
}
