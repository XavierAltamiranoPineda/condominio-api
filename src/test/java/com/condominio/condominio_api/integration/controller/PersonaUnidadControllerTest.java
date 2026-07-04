package com.condominio.condominio_api.integration.controller;

import com.condominio.condominio_api.controller.PersonaUnidadController;
import com.condominio.condominio_api.dto.request.PersonaUnidadRequest;
import com.condominio.condominio_api.dto.response.PersonaUnidadResponse;
import com.condominio.condominio_api.entity.PersonaUnidad.EstadoPersonaUnidad;
import com.condominio.condominio_api.entity.PersonaUnidad.TipoPersonaUnidad;
import com.condominio.condominio_api.security.JwtTokenProvider;
import com.condominio.condominio_api.service.interfaces.PersonaUnidadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonaUnidadController.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonaUnidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonaUnidadService personaUnidadService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;

    private PersonaUnidadRequest request;
    private PersonaUnidadResponse response;

    @BeforeEach
    void setUp() {
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
                .estado(EstadoPersonaUnidad.ACTIVO)
                .build();
    }

    @Test
    @DisplayName("✓ GET /api/v1/personas-unidades: lista relaciones")
    void shouldReturn200_whenListingPersonasUnidades() throws Exception {
        Page<PersonaUnidadResponse> page = new PageImpl<>(List.of(response));
        when(personaUnidadService.findAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/personas-unidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].tipo").value("PROPIETARIO"));
    }

    @Test
    @DisplayName("✓ POST /api/v1/personas-unidades: crea relación")
    void shouldReturn201_whenCreatePersonaUnidad() throws Exception {
        when(personaUnidadService.create(any(PersonaUnidadRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/personas-unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }
}
