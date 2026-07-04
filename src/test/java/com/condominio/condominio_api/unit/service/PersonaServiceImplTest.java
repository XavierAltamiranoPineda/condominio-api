package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.PersonaRequest;
import com.condominio.condominio_api.dto.response.PersonaResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Persona.EstadoPersona;
import com.condominio.condominio_api.entity.Persona.TipoIdentificacion;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.PersonaMapper;
import com.condominio.condominio_api.repository.PersonaRepository;
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
class PersonaServiceImplTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private PersonaMapper personaMapper;

    @Mock
    private PostgresAuditInterceptor auditInterceptor;

    @InjectMocks
    private PersonaServiceImpl personaService;

    private Persona persona;
    private PersonaRequest request;
    private PersonaResponse response;

    @BeforeEach
    void setUp() {
        persona = new Persona();
        persona.setId(1L);
        persona.setTipoIdentificacion(TipoIdentificacion.CEDULA);
        persona.setNumeroIdentificacion("1700000000");
        persona.setNombres("Juan");
        persona.setApellidos("Pérez");
        persona.setCorreo("juan@test.com");
        persona.setEstado(EstadoPersona.ACTIVO);

        request = new PersonaRequest();
        request.setTipoIdentificacion(TipoIdentificacion.CEDULA);
        request.setNumeroIdentificacion("1700000000");
        request.setNombres("Juan");
        request.setApellidos("Pérez");
        request.setCorreo("juan@test.com");
        request.setEstado(EstadoPersona.ACTIVO);

        response = PersonaResponse.builder()
                .id(1L)
                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                .numeroIdentificacion("1700000000")
                .nombres("Juan")
                .apellidos("Pérez")
                .correo("juan@test.com")
                .estado(EstadoPersona.ACTIVO)
                .build();
    }

    @Test
    @DisplayName("✓ findById: debe retornar persona si existe")
    void shouldReturnPersona_whenIdExists() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(personaMapper.toResponse(persona)).thenReturn(response);

        PersonaResponse result = personaService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCorreo()).isEqualTo("juan@test.com");
        verify(personaRepository).findById(1L);
    }

    @Test
    @DisplayName("✗ findById: debe lanzar ResourceNotFoundException si no existe")
    void shouldThrowException_whenIdNotExists() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personaService.findById(99L));
        verify(personaRepository).findById(99L);
    }

    @Test
    @DisplayName("✓ findAll: debe retornar página de personas")
    void shouldReturnPage_whenFindAll() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Persona> page = new PageImpl<>(List.of(persona));
        
        when(personaRepository.findAll(pageRequest)).thenReturn(page);
        when(personaMapper.toResponse(any(Persona.class))).thenReturn(response);

        Page<PersonaResponse> result = personaService.findAll(pageRequest);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCorreo()).isEqualTo("juan@test.com");
        verify(personaRepository).findAll(pageRequest);
    }

    @Test
    @DisplayName("✓ create: debe crear y retornar persona")
    void shouldReturnPersona_whenCreate() {
        when(personaRepository.existsByTipoIdentificacionAndNumeroIdentificacionIgnoreCase(request.getTipoIdentificacion(), request.getNumeroIdentificacion())).thenReturn(false);
        when(personaRepository.existsByCorreoIgnoreCase(request.getCorreo())).thenReturn(false);
        when(personaMapper.toEntity(request)).thenReturn(persona);
        when(personaRepository.save(persona)).thenReturn(persona);
        when(personaMapper.toResponse(persona)).thenReturn(response);

        PersonaResponse result = personaService.create(request);

        assertThat(result.getCorreo()).isEqualTo("juan@test.com");
        verify(auditInterceptor).setUsuarioActual();
        verify(personaRepository).save(persona);
    }

    @Test
    @DisplayName("✗ create: debe lanzar ResourceAlreadyExistsException si identificación duplicada")
    void shouldThrowException_whenCreateDuplicateIdentificacion() {
        when(personaRepository.existsByTipoIdentificacionAndNumeroIdentificacionIgnoreCase(request.getTipoIdentificacion(), request.getNumeroIdentificacion())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> personaService.create(request));
        verify(personaRepository, never()).save(any());
    }

    @Test
    @DisplayName("✗ create: debe lanzar ResourceAlreadyExistsException si correo duplicado")
    void shouldThrowException_whenCreateDuplicateCorreo() {
        when(personaRepository.existsByTipoIdentificacionAndNumeroIdentificacionIgnoreCase(request.getTipoIdentificacion(), request.getNumeroIdentificacion())).thenReturn(false);
        when(personaRepository.existsByCorreoIgnoreCase(request.getCorreo())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> personaService.create(request));
        verify(personaRepository, never()).save(any());
    }

    @Test
    @DisplayName("✓ update: debe actualizar y retornar persona")
    void shouldReturnPersona_whenUpdate() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(personaRepository.existsByTipoIdentificacionAndNumeroIdentificacionIgnoreCaseAndIdNot(request.getTipoIdentificacion(), request.getNumeroIdentificacion(), 1L)).thenReturn(false);
        when(personaRepository.existsByCorreoIgnoreCaseAndIdNot(request.getCorreo(), 1L)).thenReturn(false);
        when(personaRepository.save(persona)).thenReturn(persona);
        when(personaMapper.toResponse(persona)).thenReturn(response);

        PersonaResponse result = personaService.update(1L, request);

        assertThat(result.getCorreo()).isEqualTo("juan@test.com");
        verify(auditInterceptor).setUsuarioActual();
        verify(personaMapper).updateEntityFromRequest(request, persona);
        verify(personaRepository).save(persona);
    }

    @Test
    @DisplayName("✓ delete: debe eliminar persona")
    void shouldDeletePersona_whenDelete() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        personaService.delete(1L);

        verify(auditInterceptor).setUsuarioActual();
        verify(personaRepository).delete(persona);
    }
}
