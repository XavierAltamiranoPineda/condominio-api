package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.CondominioRequest;
import com.condominio.condominio_api.dto.response.CondominioResponse;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.CondominioMapper;
import com.condominio.condominio_api.repository.CondominioRepository;
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
class CondominioServiceImplTest {

    @Mock
    private CondominioRepository condominioRepository;

    @Mock
    private CondominioMapper condominioMapper;

    @Mock
    private PostgresAuditInterceptor auditInterceptor;

    @InjectMocks
    private CondominioServiceImpl condominioService;

    private Condominio condominio;
    private CondominioRequest request;
    private CondominioResponse response;

    @BeforeEach
    void setUp() {
        condominio = new Condominio();
        condominio.setId(1L);
        condominio.setNombre("Edificio Las Camelias");
        condominio.setDireccion("Calle 123");
        condominio.setTelefono("099999999");
        condominio.setEmail("admin@camelias.com");

        request = new CondominioRequest();
        request.setNombre("Edificio Las Camelias");
        request.setDireccion("Calle 123");
        request.setTelefono("099999999");
        request.setEmail("admin@camelias.com");

        response = CondominioResponse.builder()
                .id(1L)
                .nombre("Edificio Las Camelias")
                .direccion("Calle 123")
                .telefono("099999999")
                .email("admin@camelias.com")
                .build();
    }

    @Test
    @DisplayName("✓ findById: debe retornar respuesta si existe")
    void shouldReturnCondominio_whenIdExists() {
        when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));
        when(condominioMapper.toResponse(condominio)).thenReturn(response);

        CondominioResponse result = condominioService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNombre()).isEqualTo("Edificio Las Camelias");
        verify(condominioRepository).findById(1L);
    }

    @Test
    @DisplayName("✗ findById: debe lanzar ResourceNotFoundException si no existe")
    void shouldThrowException_whenIdNotExists() {
        when(condominioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> condominioService.findById(99L));
        verify(condominioRepository).findById(99L);
    }

    @Test
    @DisplayName("✓ findAll: debe retornar página de respuestas")
    void shouldReturnPage_whenFindAll() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Condominio> page = new PageImpl<>(List.of(condominio));
        
        when(condominioRepository.findAll(pageRequest)).thenReturn(page);
        when(condominioMapper.toResponse(any(Condominio.class))).thenReturn(response);

        Page<CondominioResponse> result = condominioService.findAll(pageRequest);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNombre()).isEqualTo("Edificio Las Camelias");
        verify(condominioRepository).findAll(pageRequest);
    }

    @Test
    @DisplayName("✓ create: debe crear y retornar condominio")
    void shouldReturnCondominio_whenCreate() {
        when(condominioRepository.existsByNombreIgnoreCase(request.getNombre())).thenReturn(false);
        when(condominioMapper.toEntity(request)).thenReturn(condominio);
        when(condominioRepository.save(condominio)).thenReturn(condominio);
        when(condominioMapper.toResponse(condominio)).thenReturn(response);

        CondominioResponse result = condominioService.create(request);

        assertThat(result.getNombre()).isEqualTo("Edificio Las Camelias");
        verify(auditInterceptor).setUsuarioActual();
        verify(condominioRepository).save(condominio);
    }

    @Test
    @DisplayName("✗ create: debe lanzar ResourceAlreadyExistsException si nombre duplicado")
    void shouldThrowException_whenCreateDuplicate() {
        when(condominioRepository.existsByNombreIgnoreCase(request.getNombre())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> condominioService.create(request));
        verify(condominioRepository, never()).save(any());
    }

    @Test
    @DisplayName("✓ update: debe actualizar y retornar condominio")
    void shouldReturnCondominio_whenUpdate() {
        when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));
        when(condominioRepository.existsByNombreIgnoreCaseAndIdNot(request.getNombre(), 1L)).thenReturn(false);
        when(condominioRepository.save(condominio)).thenReturn(condominio);
        when(condominioMapper.toResponse(condominio)).thenReturn(response);

        CondominioResponse result = condominioService.update(1L, request);

        assertThat(result.getNombre()).isEqualTo("Edificio Las Camelias");
        verify(auditInterceptor).setUsuarioActual();
        verify(condominioMapper).updateEntityFromRequest(request, condominio);
        verify(condominioRepository).save(condominio);
    }

    @Test
    @DisplayName("✗ update: debe lanzar ResourceAlreadyExistsException si nombre ya pertenece a otro")
    void shouldThrowException_whenUpdateDuplicate() {
        when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));
        when(condominioRepository.existsByNombreIgnoreCaseAndIdNot(request.getNombre(), 1L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> condominioService.update(1L, request));
        verify(condominioRepository, never()).save(any());
    }

    @Test
    @DisplayName("✓ delete: debe eliminar condominio")
    void shouldDeleteCondominio_whenDelete() {
        when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));

        condominioService.delete(1L);

        verify(auditInterceptor).setUsuarioActual();
        verify(condominioRepository).delete(condominio);
    }
}
