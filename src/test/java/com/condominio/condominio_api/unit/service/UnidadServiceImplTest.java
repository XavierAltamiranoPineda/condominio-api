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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnidadServiceImplTest {

    @Mock
    private UnidadRepository unidadRepository;
    @Mock
    private CondominioRepository condominioRepository;
    @Mock
    private TorreRepository torreRepository;
    @Mock
    private EstadoUnidadRepository estadoUnidadRepository;
    @Mock
    private UnidadMapper unidadMapper;
    @Mock
    private PostgresAuditInterceptor auditInterceptor;

    @InjectMocks
    private UnidadServiceImpl unidadService;

    private Condominio condominio;
    private Torre torre;
    private EstadoUnidad estado;
    private Unidad unidad;
    private UnidadRequest request;
    private UnidadResponse response;

    @BeforeEach
    void setUp() {
        condominio = new Condominio();
        condominio.setId(1L);

        torre = new Torre();
        torre.setId(1L);
        torre.setCondominio(condominio);

        estado = new EstadoUnidad();
        estado.setId(1L);
        estado.setNombre("OCUPADA");

        unidad = new Unidad();
        unidad.setId(1L);
        unidad.setCondominio(condominio);
        unidad.setTorre(torre);
        unidad.setEstado(estado);
        unidad.setNumero("101");
        unidad.setTipo(TipoUnidadEnum.DEPARTAMENTO);

        request = new UnidadRequest();
        request.setCondominioId(1L);
        request.setTorreId(1L);
        request.setEstadoId(1L);
        request.setNumero("101");
        request.setTipo(TipoUnidadEnum.DEPARTAMENTO);
        request.setAlicuota(new BigDecimal("10.5"));

        response = UnidadResponse.builder()
                .id(1L)
                .condominioId(1L)
                .torreId(1L)
                .estadoId(1L)
                .numero("101")
                .tipo(TipoUnidadEnum.DEPARTAMENTO)
                .build();
    }

    @Test
    @DisplayName("✓ create: debe crear y retornar unidad")
    void shouldCreateUnidad() {
        when(condominioRepository.findById(request.getCondominioId())).thenReturn(Optional.of(condominio));
        when(torreRepository.findById(request.getTorreId())).thenReturn(Optional.of(torre));
        when(estadoUnidadRepository.findById(request.getEstadoId())).thenReturn(Optional.of(estado));
        when(unidadRepository.existsByCondominioIdAndNumeroIgnoreCase(1L, "101")).thenReturn(false);
        when(unidadMapper.toEntity(request)).thenReturn(unidad);
        when(unidadRepository.save(unidad)).thenReturn(unidad);
        when(unidadMapper.toResponse(unidad)).thenReturn(response);

        UnidadResponse result = unidadService.create(request);

        assertThat(result.getNumero()).isEqualTo("101");
        verify(auditInterceptor).setUsuarioActual();
        verify(unidadRepository).save(unidad);
    }

    @Test
    @DisplayName("✗ create: debe lanzar error si torre no pertenece a condominio")
    void shouldThrowExceptionIfTorreNotInCondominio() {
        Condominio otroCondominio = new Condominio();
        otroCondominio.setId(2L);
        torre.setCondominio(otroCondominio);

        when(condominioRepository.findById(request.getCondominioId())).thenReturn(Optional.of(condominio));
        when(torreRepository.findById(request.getTorreId())).thenReturn(Optional.of(torre));

        assertThrows(BusinessException.class, () -> unidadService.create(request));
    }

    @Test
    @DisplayName("✗ update: debe lanzar error si hay duplicidad")
    void shouldThrowExceptionIfDuplicateOnUpdate() {
        when(unidadRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(unidad));
        when(unidadRepository.existsByCondominioIdAndNumeroIgnoreCaseAndIdNot(1L, "101", 1L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> unidadService.update(1L, request));
    }
}
