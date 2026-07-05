package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.PagoRequest;
import com.condominio.condominio_api.dto.response.PagoResponse;
import com.condominio.condominio_api.entity.Cuota;
import com.condominio.condominio_api.entity.Cuota.EstadoCuota;
import com.condominio.condominio_api.entity.EstadoPago;
import com.condominio.condominio_api.entity.Pago;
import com.condominio.condominio_api.exception.BusinessException;
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

import java.math.BigDecimal;
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

        if (cuota.getEstado() == EstadoCuota.PAGADA_TOTAL) {
            throw new BusinessException("No se pueden registrar pagos a una cuota que ya está pagada en su totalidad.");
        }
        if (cuota.getEstado() == EstadoCuota.ANULADA) {
            throw new BusinessException("No se pueden registrar pagos a una cuota anulada.");
        }

        EstadoPago estado = estadoPagoRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new ResourceNotFoundException("EstadoPago", "id", request.getEstadoId()));

        auditInterceptor.setUsuarioActual();
        Pago pago = pagoMapper.toEntity(request);
        pago.setCuota(cuota);
        pago.setEstado(estado);
        if (pago.getFecha() == null) {
            pago.setFecha(OffsetDateTime.now());
        }
        
        validarSobrepago(cuota, pago, null);

        pago = pagoRepository.save(pago);
        
        recalcularEstadoCuota(cuota);

        log.info("Pago creado: id={}", pago.getId());
        return pagoMapper.toResponse(pago);
    }

    @Override
    public PagoResponse update(Long id, PagoRequest request) {
        Pago pago = pagoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", id));

        Cuota cuota = pago.getCuota();
        if (!cuota.getId().equals(request.getCuotaId())) {
            cuota = cuotaRepository.findById(request.getCuotaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuota", "id", request.getCuotaId()));
        }

        EstadoPago estado = pago.getEstado();
        if (!estado.getId().equals(request.getEstadoId())) {
            estado = estadoPagoRepository.findById(request.getEstadoId())
                    .orElseThrow(() -> new ResourceNotFoundException("EstadoPago", "id", request.getEstadoId()));
        }

        auditInterceptor.setUsuarioActual();
        
        Pago pagoSimulado = new Pago();
        pagoSimulado.setValor(request.getValor() != null ? request.getValor() : pago.getValor());
        pagoSimulado.setEstado(estado);
        validarSobrepago(cuota, pagoSimulado, pago.getId());

        pagoMapper.updateEntityFromRequest(request, pago);
        pago.setCuota(cuota);
        pago.setEstado(estado);
        if (request.getFecha() != null) {
            pago.setFecha(request.getFecha());
        }
        pago = pagoRepository.save(pago);
        
        recalcularEstadoCuota(cuota);
        
        if (!cuota.getId().equals(pago.getCuota().getId())) {
            recalcularEstadoCuota(pago.getCuota());
        }

        log.info("Pago actualizado: id={}", pago.getId());
        return pagoMapper.toResponse(pago);
    }

    @Override
    public void delete(Long id) {
        Pago pago = pagoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", id));

        Cuota cuota = pago.getCuota();

        auditInterceptor.setUsuarioActual();
        pagoRepository.delete(pago);
        
        recalcularEstadoCuota(cuota);

        log.info("Pago eliminado: id={}", id);
    }
    
    private void validarSobrepago(Cuota cuota, Pago pagoSimulado, Long pagoIdIgnorar) {
        if (!"CONFIRMADO".equals(pagoSimulado.getEstado().getNombre())) {
            return; 
        }
        
        BigDecimal pagado = pagoRepository.sumPagosConfirmadosByCuotaId(cuota.getId());
        
        if (pagoIdIgnorar != null) {
            Pago pagoExistente = pagoRepository.findById(pagoIdIgnorar).orElse(null);
            if (pagoExistente != null && "CONFIRMADO".equals(pagoExistente.getEstado().getNombre())) {
                pagado = pagado.subtract(pagoExistente.getValor());
            }
        }
        
        BigDecimal nuevoTotal = pagado.add(pagoSimulado.getValor());
        
        if (nuevoTotal.compareTo(cuota.getValor()) > 0) {
            BigDecimal exceso = nuevoTotal.subtract(cuota.getValor());
            throw new BusinessException(String.format("El pago excede el valor de la cuota por $%s. Saldo pendiente real: $%s", 
                    exceso, cuota.getValor().subtract(pagado)));
        }
    }

    private void recalcularEstadoCuota(Cuota cuota) {
        if (cuota.getEstado() == EstadoCuota.ANULADA) return;

        BigDecimal totalPagado = pagoRepository.sumPagosConfirmadosByCuotaId(cuota.getId());

        if (totalPagado.compareTo(BigDecimal.ZERO) == 0) {
            cuota.setEstado(EstadoCuota.PENDIENTE);
        } else if (totalPagado.compareTo(cuota.getValor()) >= 0) {
            cuota.setEstado(EstadoCuota.PAGADA_TOTAL);
        } else {
            cuota.setEstado(EstadoCuota.PAGADA_PARCIAL);
        }
        
        cuotaRepository.save(cuota);
        log.info("Estado de cuota recalculado: id={}, estado={}", cuota.getId(), cuota.getEstado());
    }
}
