package com.condominio.condominio_api.service.impl;

import com.condominio.condominio_api.audit.PostgresAuditInterceptor;
import com.condominio.condominio_api.dto.request.PersonaUnidadRequest;
import com.condominio.condominio_api.dto.response.PersonaUnidadResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.PersonaUnidad;
import com.condominio.condominio_api.entity.PersonaUnidad.EstadoPersonaUnidad;
import com.condominio.condominio_api.entity.PersonaUnidad.TipoPersonaUnidad;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.exception.BusinessException;
import com.condominio.condominio_api.exception.ResourceAlreadyExistsException;
import com.condominio.condominio_api.exception.ResourceNotFoundException;
import com.condominio.condominio_api.mapper.PersonaUnidadMapper;
import com.condominio.condominio_api.repository.PersonaRepository;
import com.condominio.condominio_api.repository.PersonaUnidadRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaUnidadServiceImplTest {

    @Mock
    private PersonaUnidadRepository personaUnidadRepository;
    @Mock
    private PersonaRepository personaRepository;
    @Mock
    private UnidadRepository unidadRepository;
    @Mock
    private PersonaUnidadMapper personaUnidadMapper;
    @Mock
    private PostgresAuditInterceptor auditInterceptor;

    @InjectMocks
    private PersonaUnidadServiceImpl personaUnidadService;

    private Persona persona;
    private Unidad unidad;
    private PersonaUnidad personaUnidad;
    private PersonaUnidadRequest request;
    private PersonaUnidadResponse response;

    @BeforeEach
    void setUp() {
        persona = new Persona();
        persona.setId(1L);

        unidad = new Unidad();
        unidad.setId(1L);

        personaUnidad = new PersonaUnidad();
        personaUnidad.setId(1L);
        personaUnidad.setPersona(persona);
        personaUnidad.setUnidad(unidad);
        personaUnidad.setTipo(TipoPersonaUnidad.PROPIETARIO);
        personaUnidad.setEstado(EstadoPersonaUnidad.ACTIVO);
        personaUnidad.setFechaInicio(LocalDate.now());

        request = new PersonaUnidadRequest();
        request.setPersonaId(1L);
        request.setUnidadId(1L);
        request.setTipo(TipoPersonaUnidad.PROPIETARIO);
        request.setEstado(EstadoPersonaUnidad.ACTIVO);
        request.setFechaInicio(LocalDate.now());

        response = PersonaUnidadResponse.builder()
                .id(1L)
                .personaId(1L)
                .unidadId(1L)
                .tipo(TipoPersonaUnidad.PROPIETARIO)
                .build();
    }

    @Test
    @DisplayName("✓ create: debe crear y retornar PersonaUnidad")
    void shouldCreatePersonaUnidad() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(unidadRepository.findById(1L)).thenReturn(Optional.of(unidad));
        when(personaUnidadRepository.existsByPersonaIdAndUnidadIdAndEstado(1L, 1L, EstadoPersonaUnidad.ACTIVO)).thenReturn(false);
        when(personaUnidadMapper.toEntity(request)).thenReturn(personaUnidad);
        when(personaUnidadRepository.save(personaUnidad)).thenReturn(personaUnidad);
        when(personaUnidadMapper.toResponse(personaUnidad)).thenReturn(response);

        PersonaUnidadResponse result = personaUnidadService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        verify(personaUnidadRepository).save(personaUnidad);
    }

    @Test
    @DisplayName("✗ create: lanza BusinessException si fechaFin es antes que fechaInicio")
    void shouldThrowException_whenFechasInvalidas() {
        request.setFechaFin(LocalDate.now().minusDays(1));
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(unidadRepository.findById(1L)).thenReturn(Optional.of(unidad));

        assertThrows(BusinessException.class, () -> personaUnidadService.create(request));
    }
}
