package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.ReciboRequest;
import com.condominio.condominio_api.dto.response.ReciboResponse;
import com.condominio.condominio_api.entity.Archivo;
import com.condominio.condominio_api.entity.Pago;
import com.condominio.condominio_api.entity.Recibo;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.ReciboMapper;
import com.condominio.condominio_api.repository.ArchivoRepository;
import com.condominio.condominio_api.repository.PagoRepository;
import com.condominio.condominio_api.repository.ReciboRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReciboServiceImplTest {

    @Mock
    private ReciboRepository reciboRepository;
    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private ArchivoRepository archivoRepository;
    @Mock
    private ReciboMapper reciboMapper;
    @Mock
    private PostgresAuditInterceptor auditInterceptor;

    @InjectMocks
    private ReciboServiceImpl reciboService;

    private Pago pago;
    private Archivo archivo;
    private Recibo recibo;
    private ReciboRequest request;
    private ReciboResponse response;

    @BeforeEach
    void setUp() {
        pago = new Pago();
        pago.setId(1L);

        archivo = new Archivo();
        archivo.setId(1L);
        archivo.setNombre("recibo.pdf");

        recibo = new Recibo();
        recibo.setId(1L);
        recibo.setNumero("REC-001");
        recibo.setPago(pago);
        recibo.setArchivo(archivo);

        request = new ReciboRequest();
        request.setNumero("REC-001");
        request.setPagoId(1L);
        request.setArchivoId(1L);

        response = ReciboResponse.builder()
                .id(1L)
                .numero("REC-001")
                .pagoId(1L)
                .archivoId(1L)
                .archivoNombre("recibo.pdf")
                .build();
    }

    @Test
    @DisplayName("✓ create: debe crear y retornar recibo")
    void shouldCreateRecibo() {
        when(reciboRepository.existsByNumeroIgnoreCase("REC-001")).thenReturn(false);
        when(reciboRepository.existsByPagoId(1L)).thenReturn(false);
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(archivoRepository.findById(1L)).thenReturn(Optional.of(archivo));
        
        when(reciboMapper.toEntity(request)).thenReturn(recibo);
        when(reciboRepository.save(any(Recibo.class))).thenReturn(recibo);
        when(reciboMapper.toResponse(recibo)).thenReturn(response);

        ReciboResponse result = reciboService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNumero()).isEqualTo("REC-001");
        verify(reciboRepository).save(recibo);
    }

    @Test
    @DisplayName("✗ create: lanza ResourceAlreadyExistsException si número duplicado")
    void shouldThrowException_whenNumeroDuplicado() {
        when(reciboRepository.existsByNumeroIgnoreCase("REC-001")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> reciboService.create(request));
    }

    @Test
    @DisplayName("✗ create: lanza ResourceAlreadyExistsException si el pago ya tiene recibo")
    void shouldThrowException_whenPagoDuplicado() {
        when(reciboRepository.existsByNumeroIgnoreCase("REC-001")).thenReturn(false);
        when(reciboRepository.existsByPagoId(1L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> reciboService.create(request));
    }
}
