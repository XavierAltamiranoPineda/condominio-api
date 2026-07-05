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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceImplTest {

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private CuotaRepository cuotaRepository;
    @Mock
    private EstadoPagoRepository estadoPagoRepository;
    @Mock
    private PagoMapper pagoMapper;
    @Mock
    private PostgresAuditInterceptor auditInterceptor;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private Cuota cuota;
    private EstadoPago estado;
    private Pago pago;
    private PagoRequest request;
    private PagoResponse response;

    @BeforeEach
    void setUp() {
        cuota = new Cuota();
        cuota.setId(1L);
        cuota.setValor(new BigDecimal("500.00"));

        estado = new EstadoPago();
        estado.setId(1L);
        estado.setNombre("CONFIRMADO");

        pago = new Pago();
        pago.setId(1L);
        pago.setCuota(cuota);
        pago.setEstado(estado);
        pago.setValor(new BigDecimal("100.00"));
        pago.setMetodo("TRANSFERENCIA");

        request = new PagoRequest();
        request.setCuotaId(1L);
        request.setEstadoId(1L);
        request.setValor(new BigDecimal("100.00"));
        request.setMetodo("TRANSFERENCIA");

        response = PagoResponse.builder()
                .id(1L)
                .cuotaId(1L)
                .estadoId(1L)
                .valor(new BigDecimal("100.00"))
                .metodo("TRANSFERENCIA")
                .build();
    }

    @Test
    @DisplayName("✓ create: debe crear y retornar pago, actualizando cuota a PAGADA_PARCIAL")
    void shouldCreatePagoAndSetParcial() {
        when(cuotaRepository.findById(1L)).thenReturn(Optional.of(cuota));
        when(estadoPagoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(pagoRepository.sumPagosConfirmadosByCuotaId(1L))
            .thenReturn(BigDecimal.ZERO)
            .thenReturn(new BigDecimal("100.00"));
        
        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(pagoMapper.toResponse(pago)).thenReturn(response);

        PagoResponse result = pagoService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(cuota.getEstado()).isEqualTo(EstadoCuota.PAGADA_PARCIAL);
        verify(pagoRepository).save(pago);
        verify(cuotaRepository).save(cuota);
    }

    @Test
    @DisplayName("✓ create: debe crear y retornar pago, actualizando cuota a PAGADA_TOTAL")
    void shouldCreatePagoAndSetTotal() {
        request.setValor(new BigDecimal("500.00")); // Igual al valor de la cuota
        pago.setValor(new BigDecimal("500.00"));
        
        when(cuotaRepository.findById(1L)).thenReturn(Optional.of(cuota));
        when(estadoPagoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(pagoRepository.sumPagosConfirmadosByCuotaId(1L))
            .thenReturn(BigDecimal.ZERO)
            .thenReturn(new BigDecimal("500.00"));
        
        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(pagoMapper.toResponse(pago)).thenReturn(response);

        pagoService.create(request);

        assertThat(cuota.getEstado()).isEqualTo(EstadoCuota.PAGADA_TOTAL);
        verify(cuotaRepository).save(cuota);
    }

    @Test
    @DisplayName("✗ create: lanza BusinessException por sobrepago")
    void shouldThrowException_whenSobrepago() {
        request.setValor(new BigDecimal("600.00")); // Supera la cuota de 500
        pago.setValor(new BigDecimal("600.00"));
        
        when(cuotaRepository.findById(1L)).thenReturn(Optional.of(cuota));
        when(estadoPagoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(pagoRepository.sumPagosConfirmadosByCuotaId(1L)).thenReturn(BigDecimal.ZERO);
        when(pagoMapper.toEntity(request)).thenReturn(pago);

        assertThrows(BusinessException.class, () -> pagoService.create(request));
    }

    @Test
    @DisplayName("✗ create: lanza BusinessException si cuota ya está PAGADA_TOTAL")
    void shouldThrowException_whenCuotaPagadaTotal() {
        cuota.setEstado(EstadoCuota.PAGADA_TOTAL);
        when(cuotaRepository.findById(1L)).thenReturn(Optional.of(cuota));

        assertThrows(BusinessException.class, () -> pagoService.create(request));
    }

    @Test
    @DisplayName("✗ create: lanza ResourceNotFoundException si cuota no existe")
    void shouldThrowException_whenCuotaNotExists() {
        when(cuotaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pagoService.create(request));
    }
}
