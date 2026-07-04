package com.condominio.condominio_api.integration.controller;

import com.condominio.condominio_api.controller.TorreController;
import com.condominio.condominio_api.dto.request.TorreRequest;
import com.condominio.condominio_api.dto.response.TorreResponse;
import com.condominio.condominio_api.security.JwtTokenProvider;
import com.condominio.condominio_api.service.interfaces.TorreService;
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

@WebMvcTest(TorreController.class)
@AutoConfigureMockMvc(addFilters = false) // Deshabilita seguridad para test unitario de capa web
class TorreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TorreService torreService;

    @MockBean
    private com.condominio.condominio_api.service.interfaces.UnidadService unidadService;

    // Se mockean beans de seguridad requeridos por el contexto web de Spring Security
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;

    private TorreRequest request;
    private TorreResponse response;

    @BeforeEach
    void setUp() {
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
    @DisplayName("✓ GET /api/v1/torres: lista torres paginadas")
    void shouldReturn200_whenListingTorres() throws Exception {
        Page<TorreResponse> page = new PageImpl<>(List.of(response));
        when(torreService.findAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/torres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].nombre").value("Torre A"));
    }

    @Test
    @DisplayName("✓ GET /api/v1/torres/{id}: retorna torre")
    void shouldReturn200_whenGetTorreById() throws Exception {
        when(torreService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/torres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Torre A"));
    }

    @Test
    @DisplayName("✓ POST /api/v1/torres: crea torre")
    void shouldReturn201_whenCreateTorre() throws Exception {
        when(torreService.create(any(TorreRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/torres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Torre A"));
    }

    @Test
    @DisplayName("✗ POST /api/v1/torres: error de validación (400)")
    void shouldReturn400_whenCreateTorreInvalid() throws Exception {
        request.setNombre(""); // Invalido: @NotBlank

        mockMvc.perform(post("/api/v1/torres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("✓ PUT /api/v1/torres/{id}: actualiza torre")
    void shouldReturn200_whenUpdateTorre() throws Exception {
        when(torreService.update(eq(1L), any(TorreRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/torres/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("✓ DELETE /api/v1/torres/{id}: elimina torre")
    void shouldReturn204_whenDeleteTorre() throws Exception {
        mockMvc.perform(delete("/api/v1/torres/1"))
                .andExpect(status().isNoContent());
    }
}
