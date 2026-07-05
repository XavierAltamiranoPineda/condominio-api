package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.PagoRequest;
import com.condominio.condominio_api.dto.response.PagoResponse;
import com.condominio.condominio_api.entity.Cuota;
import com.condominio.condominio_api.entity.EstadoPago;
import com.condominio.condominio_api.entity.Pago;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.PagoMapper;
import com.condominio.condominio_api.repository.CuotaRepository;
import com.condominio.condominio_api.repository.EstadoPagoRepository;
import com.condominio.condominio_api.repository.PagoRepository;
import com.condominio.condominio_api.service.interfaces.PagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final CuotaRepository cuotaRepository;
    private final EstadoPagoRepository estadoPagoRepository;
    private final PagoMapper pagoMapper;
    private final PostgresAuditInterceptor auditInterceptor;

    @Override
    public PagoResponse findById(Long id) {
        return pagoRepository.findByIdWithDetails(id)
                .map(pagoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", id));
    }

    @Override
    public Page<PagoResponse> findAll(Pageable pageable) {
        return pagoRepository.findAllWithDetails(pageable)
                .map(pagoMapper::toResponse);
    }

    @Override
    public Page<PagoResponse> findByCuotaId(Long cuotaId, Pageable pageable) {
        if (!cuotaRepository.existsById(cuotaId)) {
            throw new ResourceNotFoundException("Cuota", "id", cuotaId);
        }
        return pagoRepository.findByCuotaIdWithDetails(cuotaId, pageable)
                .map(pagoMapper::toResponse);
    }

    @Override
    public PagoResponse create(PagoRequest request) {
        Cuota cuota = cuotaRepository.findById(request.getCuotaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cuota", "id", request.getCuotaId()));

        EstadoPago estado = estadoPagoRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("EstadoPago", "id", request.getEstadoId()));

        auditInterceptor.setUsuarioActual();
        Pago pago = pagoMapper.toEntity(request);
        pago.setCuota(cuota);
        pago.setEstado(estado);
        if (pago.getFecha() == null) {
            pago.setFecha(OffsetDateTime.now());
        }
        pago = pagoRepository.save(pago);

        log.info("Pago creado: id={}", pago.getId());
        return pagoMapper.toResponse(pago);
    }

    @Override
    public PagoResponse update(Long id, PagoRequest request) {
        Pago pago = pagoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", id));

        if (!pago.getCuota().getId().equals(request.getCuotaId())) {
            Cuota cuota = cuotaRepository.findById(request.getCuotaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuota", "id", request.getCuotaId()));
            pago.setCuota(cuota);
        }

        if (!pago.getEstado().getId().equals(request.getEstadoId())) {
            EstadoPago estado = estadoPagoRepository.findById(request.getEstadoId())
                    .orElseThrow(() -> new ResourceNotFoundException("EstadoPago", "id", request.getEstadoId()));
            pago.setEstado(estado);
        }

        auditInterceptor.setUsuarioActual();
        pagoMapper.updateEntityFromRequest(request, pago);
        if (request.getFecha() != null) {
            pago.setFecha(request.getFecha());
        }
        pago = pagoRepository.save(pago);

        log.info("Pago actualizado: id={}", pago.getId());
        return pagoMapper.toResponse(pago);
    }

    @Override
    public void delete(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", id));

        auditInterceptor.setUsuarioActual();
        pagoRepository.delete(pago);

        log.info("Pago eliminado: id={}", id);
    }
}
