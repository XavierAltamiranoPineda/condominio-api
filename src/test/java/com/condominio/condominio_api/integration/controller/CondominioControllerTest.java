package com.condominio.condominio_api.integration.controller;

import com.condominio.condominio_api.controller.CondominioController;
import com.condominio.condominio_api.dto.request.CondominioRequest;
import com.condominio.condominio_api.dto.response.CondominioResponse;
import com.condominio.condominio_api.security.JwtTokenProvider;
import com.condominio.condominio_api.service.interfaces.CondominioService;
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

@WebMvcTest(CondominioController.class)
@AutoConfigureMockMvc(addFilters = false) // Deshabilita seguridad para test unitario de capa web
class CondominioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CondominioService condominioService;

    @MockBean
    private com.condominio.condominio_api.service.interfaces.TorreService torreService;

    // Se mockean beans de seguridad requeridos por el contexto web de Spring Security
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;

    private CondominioRequest request;
    private CondominioResponse response;

    @BeforeEach
    void setUp() {
        request = new CondominioRequest();
        request.setNombre("Edificio Test");

        response = CondominioResponse.builder()
                .id(1L)
                .nombre("Edificio Test")
                .build();
    }

    @Test
    @DisplayName("✓ GET /api/v1/condominios: lista condominios paginados")
    void shouldReturn200_whenListingCondominios() throws Exception {
        Page<CondominioResponse> page = new PageImpl<>(List.of(response));
        when(condominioService.findAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/condominios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].nombre").value("Edificio Test"));
    }

    @Test
    @DisplayName("✓ GET /api/v1/condominios/{id}: retorna condominio")
    void shouldReturn200_whenGetCondominioById() throws Exception {
        when(condominioService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/condominios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Edificio Test"));
    }

    @Test
    @DisplayName("✓ POST /api/v1/condominios: crea condominio")
    void shouldReturn201_whenCreateCondominio() throws Exception {
        when(condominioService.create(any(CondominioRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/condominios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Edificio Test"));
    }

    @Test
    @DisplayName("✗ POST /api/v1/condominios: error de validación (400)")
    void shouldReturn400_whenCreateCondominioInvalid() throws Exception {
        request.setNombre(""); // Invalido: @NotBlank

        mockMvc.perform(post("/api/v1/condominios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("✓ PUT /api/v1/condominios/{id}: actualiza condominio")
    void shouldReturn200_whenUpdateCondominio() throws Exception {
        when(condominioService.update(eq(1L), any(CondominioRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/condominios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("✓ DELETE /api/v1/condominios/{id}: elimina condominio")
    void shouldReturn204_whenDeleteCondominio() throws Exception {
        mockMvc.perform(delete("/api/v1/condominios/1"))
                .andExpect(status().isNoContent());
    }
}
