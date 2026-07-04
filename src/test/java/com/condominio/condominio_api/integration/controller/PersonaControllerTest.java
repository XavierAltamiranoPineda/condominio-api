package com.condominio.condominio_api.integration.controller;

import com.condominio.condominio_api.controller.PersonaController;
import com.condominio.condominio_api.dto.request.PersonaRequest;
import com.condominio.condominio_api.dto.response.PersonaResponse;
import com.condominio.condominio_api.entity.Persona.EstadoPersona;
import com.condominio.condominio_api.entity.Persona.TipoIdentificacion;
import com.condominio.condominio_api.security.JwtTokenProvider;
import com.condominio.condominio_api.service.interfaces.PersonaService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonaController.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonaService personaService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;

    private PersonaRequest request;
    private PersonaResponse response;

    @BeforeEach
    void setUp() {
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
    @DisplayName("✓ GET /api/v1/personas: lista personas paginadas")
    void shouldReturn200_whenListingPersonas() throws Exception {
        Page<PersonaResponse> page = new PageImpl<>(List.of(response));
        when(personaService.findAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/personas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].nombres").value("Juan"));
    }

    @Test
    @DisplayName("✓ GET /api/v1/personas/{id}: retorna persona")
    void shouldReturn200_whenGetPersonaById() throws Exception {
        when(personaService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/personas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombres").value("Juan"));
    }

    @Test
    @DisplayName("✓ POST /api/v1/personas: crea persona")
    void shouldReturn201_whenCreatePersona() throws Exception {
        when(personaService.create(any(PersonaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombres").value("Juan"));
    }

    @Test
    @DisplayName("✗ POST /api/v1/personas: error de validación (400)")
    void shouldReturn400_whenCreatePersonaInvalid() throws Exception {
        request.setCorreo("invalid-email"); // Invalido: @Email

        mockMvc.perform(post("/api/v1/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("✓ PUT /api/v1/personas/{id}: actualiza persona")
    void shouldReturn200_whenUpdatePersona() throws Exception {
        when(personaService.update(eq(1L), any(PersonaRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/personas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("✓ DELETE /api/v1/personas/{id}: elimina persona")
    void shouldReturn204_whenDeletePersona() throws Exception {
        mockMvc.perform(delete("/api/v1/personas/1"))
                .andExpect(status().isNoContent());
    }
}
