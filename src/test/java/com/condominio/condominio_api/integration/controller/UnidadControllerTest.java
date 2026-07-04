package com.condominio.condominio_api.integration.controller;

import com.condominio.condominio_api.controller.UnidadController;
import com.condominio.condominio_api.dto.request.UnidadRequest;
import com.condominio.condominio_api.dto.response.UnidadResponse;
import com.condominio.condominio_api.entity.enums.TipoUnidadEnum;
import com.condominio.condominio_api.security.JwtTokenProvider;
import com.condominio.condominio_api.service.interfaces.UnidadService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UnidadController.class)
@AutoConfigureMockMvc(addFilters = false)
class UnidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UnidadService unidadService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    private UnidadRequest request;
    private UnidadResponse response;

    @BeforeEach
    void setUp() {
        request = new UnidadRequest();
        request.setCondominioId(1L);
        request.setEstadoId(1L);
        request.setNumero("101");
        request.setTipo(TipoUnidadEnum.DEPARTAMENTO);
        request.setAlicuota(new BigDecimal("10.50"));

        response = UnidadResponse.builder()
                .id(1L)
                .condominioId(1L)
                .numero("101")
                .tipo(TipoUnidadEnum.DEPARTAMENTO)
                .build();
    }

    @Test
    @DisplayName("✓ GET /api/v1/unidades: lista unidades")
    void shouldReturn200_whenListingUnidades() throws Exception {
        Page<UnidadResponse> page = new PageImpl<>(List.of(response));
        when(unidadService.findAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/unidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].numero").value("101"));
    }
    
    @Test
    @DisplayName("✓ POST /api/v1/unidades: crea unidad")
    void shouldReturn201_whenCreateUnidad() throws Exception {
        when(unidadService.create(any(UnidadRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }
}
