package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.CuotaRequest;
import com.condominio.condominio_api.dto.response.CuotaResponse;
import com.condominio.condominio_api.entity.Cuota;
import com.condominio.condominio_api.entity.Cuota.EstadoCuota;
import com.condominio.condominio_api.entity.Cuota.TipoCuota;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.CuotaMapper;
import com.condominio.condominio_api.repository.CuotaRepository;
import com.condominio.condominio_api.repository.UnidadRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuotaServiceImplTest {

    @Mock
    private CuotaRepository cuotaRepository;
    @Mock
    private UnidadRepository unidadRepository;
    @Mock
    private CuotaMapper cuotaMapper;
    @Mock
    private PostgresAuditInterceptor auditInterceptor;

    @InjectMocks
    private CuotaServiceImpl cuotaService;

    private Unidad unidad;
    private Cuota cuota;
    private CuotaRequest request;
    private CuotaResponse response;

    @BeforeEach
    void setUp() {
        unidad = new Unidad();
        unidad.setId(1L);

        cuota = new Cuota();
        cuota.setId(1L);
        cuota.setUnidad(unidad);
        cuota.setMes((short) 1);
        cuota.setAnio((short) 2024);
        cuota.setValor(new BigDecimal("100.00"));
        cuota.setTipo(TipoCuota.ORDINARIA);
        cuota.setFechaVencimiento(LocalDate.now().plusDays(10));
        cuota.setEstado(EstadoCuota.PENDIENTE);

        request = new CuotaRequest();
        request.setUnidadId(1L);
        request.setMes((short) 1);
        request.setAnio((short) 2024);
        request.setValor(new BigDecimal("100.00"));
        request.setTipo(TipoCuota.ORDINARIA);
        request.setFechaVencimiento(LocalDate.now().plusDays(10));
        request.setEstado(EstadoCuota.PENDIENTE);

        response = CuotaResponse.builder()
                .id(1L)
                .unidadId(1L)
                .mes((short) 1)
                .anio((short) 2024)
                .tipo(TipoCuota.ORDINARIA)
                .build();
    }

    @Test
    @DisplayName("✓ create: debe crear y retornar cuota")
    void shouldCreateCuota() {
        when(unidadRepository.findById(1L)).thenReturn(Optional.of(unidad));
        when(cuotaRepository.existsByUnidadIdAndMesAndAnioAndTipo(1L, (short) 1, (short) 2024, TipoCuota.ORDINARIA)).thenReturn(false);
        when(cuotaMapper.toEntity(request)).thenReturn(cuota);
        when(cuotaRepository.save(cuota)).thenReturn(cuota);
        when(cuotaMapper.toResponse(cuota)).thenReturn(response);

        CuotaResponse result = cuotaService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        verify(cuotaRepository).save(cuota);
    }

    @Test
    @DisplayName("✗ create: lanza ResourceAlreadyExistsException si cuota está duplicada")
    void shouldThrowException_whenCuotaDuplicada() {
        when(unidadRepository.findById(1L)).thenReturn(Optional.of(unidad));
        when(cuotaRepository.existsByUnidadIdAndMesAndAnioAndTipo(1L, (short) 1, (short) 2024, TipoCuota.ORDINARIA)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> cuotaService.create(request));
    }
}
