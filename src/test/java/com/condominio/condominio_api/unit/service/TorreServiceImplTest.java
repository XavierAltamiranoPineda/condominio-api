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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorreServiceImplTest {

    @Mock
    private TorreRepository torreRepository;

    @Mock
    private CondominioRepository condominioRepository;

    @Mock
    private TorreMapper torreMapper;

    @Mock
    private PostgresAuditInterceptor auditInterceptor;

    @InjectMocks
    private TorreServiceImpl torreService;

    private Torre torre;
    private Condominio condominio;
    private TorreRequest request;
    private TorreResponse response;

    @BeforeEach
    void setUp() {
        condominio = new Condominio();
        condominio.setId(1L);
        condominio.setNombre("Edificio Las Camelias");

        torre = new Torre();
        torre.setId(1L);
        torre.setCondominio(condominio);
        torre.setNombre("Torre A");

        request = new TorreRequest();
        request.setCondominioId(1L);
        request.setNombre("Torre A");

        response = TorreResponse.builder()
                .id(1L)
                .condominioId(1L)
                .condominioNombre("Edificio Las Camelias")
                .nombre("Torre A")
                .build();
    }

    @Test
    @DisplayName("✓ findById: debe retornar torre si existe")
    void shouldReturnTorre_whenIdExists() {
        when(torreRepository.findByIdWithCondominio(1L)).thenReturn(Optional.of(torre));
        when(torreMapper.toResponse(torre)).thenReturn(response);

        TorreResponse result = torreService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNombre()).isEqualTo("Torre A");
        verify(torreRepository).findByIdWithCondominio(1L);
    }

    @Test
    @DisplayName("✗ findById: debe lanzar ResourceNotFoundException si no existe")
    void shouldThrowException_whenIdNotExists() {
        when(torreRepository.findByIdWithCondominio(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> torreService.findById(99L));
        verify(torreRepository).findByIdWithCondominio(99L);
    }

    @Test
    @DisplayName("✓ findAll: debe retornar página de torres")
    void shouldReturnPage_whenFindAll() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Torre> page = new PageImpl<>(List.of(torre));
        
        when(torreRepository.findAllWithCondominio(pageRequest)).thenReturn(page);
        when(torreMapper.toResponse(any(Torre.class))).thenReturn(response);

        Page<TorreResponse> result = torreService.findAll(pageRequest);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNombre()).isEqualTo("Torre A");
        verify(torreRepository).findAllWithCondominio(pageRequest);
    }

    @Test
    @DisplayName("✓ create: debe crear y retornar torre")
    void shouldReturnTorre_whenCreate() {
        when(condominioRepository.findById(request.getCondominioId())).thenReturn(Optional.of(condominio));
        when(torreRepository.existsByNombreIgnoreCaseAndCondominioId(request.getNombre(), condominio.getId())).thenReturn(false);
        when(torreMapper.toEntity(request)).thenReturn(torre);
        when(torreRepository.save(torre)).thenReturn(torre);
        when(torreMapper.toResponse(torre)).thenReturn(response);

        TorreResponse result = torreService.create(request);

        assertThat(result.getNombre()).isEqualTo("Torre A");
        verify(auditInterceptor).setUsuarioActual();
        verify(torreRepository).save(torre);
    }

    @Test
    @DisplayName("✗ create: debe lanzar ResourceAlreadyExistsException si nombre de torre duplicado en el mismo condominio")
    void shouldThrowException_whenCreateDuplicate() {
        when(condominioRepository.findById(request.getCondominioId())).thenReturn(Optional.of(condominio));
        when(torreRepository.existsByNombreIgnoreCaseAndCondominioId(request.getNombre(), condominio.getId())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> torreService.create(request));
        verify(torreRepository, never()).save(any());
    }

    @Test
    @DisplayName("✗ create: debe lanzar ResourceNotFoundException si el condominio no existe")
    void shouldThrowException_whenCondominioNotExistsOnCreate() {
        when(condominioRepository.findById(request.getCondominioId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> torreService.create(request));
        verify(torreRepository, never()).save(any());
    }

    @Test
    @DisplayName("✓ update: debe actualizar y retornar torre")
    void shouldReturnTorre_whenUpdate() {
        when(torreRepository.findByIdWithCondominio(1L)).thenReturn(Optional.of(torre));
        when(torreRepository.existsByNombreIgnoreCaseAndCondominioIdAndIdNot(request.getNombre(), condominio.getId(), 1L)).thenReturn(false);
        when(torreRepository.save(torre)).thenReturn(torre);
        when(torreMapper.toResponse(torre)).thenReturn(response);

        TorreResponse result = torreService.update(1L, request);

        assertThat(result.getNombre()).isEqualTo("Torre A");
        verify(auditInterceptor).setUsuarioActual();
        verify(torreMapper).updateEntityFromRequest(request, torre);
        verify(torreRepository).save(torre);
    }

    @Test
    @DisplayName("✓ delete: debe eliminar torre")
    void shouldDeleteTorre_whenDelete() {
        when(torreRepository.findById(1L)).thenReturn(Optional.of(torre));

        torreService.delete(1L);

        verify(auditInterceptor).setUsuarioActual();
        verify(torreRepository).delete(torre);
    }
}
