package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.CuotaRequest;
import com.condominio.condominio_api.dto.response.CuotaResponse;
import com.condominio.condominio_api.entity.Cuota;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.CuotaMapper;
import com.condominio.condominio_api.repository.CuotaRepository;
import com.condominio.condominio_api.repository.UnidadRepository;
import com.condominio.condominio_api.service.interfaces.CuotaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuotaServiceImpl implements CuotaService {

    private final CuotaRepository cuotaRepository;
    private final UnidadRepository unidadRepository;
    private final CuotaMapper cuotaMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public CuotaResponse findById(Long id) {
        return cuotaRepository.findByIdWithDetails(id)
                .map(cuotaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cuota", "id", id));
    }

    @Override
    public Page<CuotaResponse> findAll(Pageable pageable) {
        return cuotaRepository.findAllWithDetails(pageable)
                .map(cuotaMapper::toResponse);
    }

    @Override
    public CuotaResponse create(CuotaRequest request) {
        Unidad unidad = unidadRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));

        if (cuotaRepository.existsByUnidadIdAndMesAndAnioAndTipo(request.getUnidadId(), request.getMes(), request.getAnio(), request.getTipo())) {
            throw new ResourceAlreadyExistsException("Cuota", "unidad+mes+anio+tipo", 
                    request.getUnidadId() + "-" + request.getMes() + "-" + request.getAnio() + "-" + request.getTipo());
        }

        auditInterceptor.setUsuarioActual();
        Cuota cuota = cuotaMapper.toEntity(request);
        cuota.setUnidad(unidad);
        cuota = cuotaRepository.save(cuota);

        log.info("Cuota creada: id={}", cuota.getId());
        return cuotaMapper.toResponse(cuota);
    }

    @Override
    public CuotaResponse update(Long id, CuotaRequest request) {
        Cuota cuota = cuotaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuota", "id", id));

        if (!cuota.getUnidad().getId().equals(request.getUnidadId())) {
            Unidad unidad = unidadRepository.findById(request.getUnidadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidad", "id", request.getUnidadId()));
            cuota.setUnidad(unidad);
        }

        if (cuotaRepository.existsByUnidadIdAndMesAndAnioAndTipoAndIdNot(request.getUnidadId(), request.getMes(), request.getAnio(), request.getTipo(), id)) {
             throw new ResourceAlreadyExistsException("Cuota", "unidad+mes+anio+tipo", 
                    request.getUnidadId() + "-" + request.getMes() + "-" + request.getAnio() + "-" + request.getTipo());
        }

        auditInterceptor.setUsuarioActual();
        cuotaMapper.updateEntityFromRequest(request, cuota);
        cuota = cuotaRepository.save(cuota);

        log.info("Cuota actualizada: id={}", cuota.getId());
        return cuotaMapper.toResponse(cuota);
    }

    @Override
    public void delete(Long id) {
        Cuota cuota = cuotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuota", "id", id));

        auditInterceptor.setUsuarioActual();
        cuotaRepository.delete(cuota);

        log.info("Cuota eliminada: id={}", id);
    }
}
