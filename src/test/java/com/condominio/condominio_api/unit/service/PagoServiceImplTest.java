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

        estado = new EstadoPago();
        estado.setId(1L);
        estado.setNombre("PAGADO");

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
    @DisplayName("✓ create: debe crear y retornar pago")
    void shouldCreatePago() {
        when(cuotaRepository.findById(1L)).thenReturn(Optional.of(cuota));
        when(estadoPagoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(pagoMapper.toEntity(request)).thenReturn(pago);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(pagoMapper.toResponse(pago)).thenReturn(response);

        PagoResponse result = pagoService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        verify(pagoRepository).save(pago);
    }

    @Test
    @DisplayName("✗ create: lanza ResourceNotFoundException si cuota no existe")
    void shouldThrowException_whenCuotaNotExists() {
        when(cuotaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pagoService.create(request));
    }
}
