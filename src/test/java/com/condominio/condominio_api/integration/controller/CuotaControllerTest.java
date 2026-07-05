package com.condominio.condominio_api.integration.controller;

import com.condominio.condominio_api.controller.CuotaController;
import com.condominio.condominio_api.dto.request.CuotaRequest;
import com.condominio.condominio_api.dto.response.CuotaResponse;
import com.condominio.condominio_api.entity.Cuota.EstadoCuota;
import com.condominio.condominio_api.entity.Cuota.TipoCuota;
import com.condominio.condominio_api.security.JwtTokenProvider;
import com.condominio.condominio_api.service.interfaces.CuotaService;
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
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CuotaController.class)
@AutoConfigureMockMvc(addFilters = false)
class CuotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CuotaService cuotaService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;

    private CuotaRequest request;
    private CuotaResponse response;

    @BeforeEach
    void setUp() {
        request = new CuotaRequest();
        request.setUnidadId(1L);
        request.setMes((short) 1);
        request.setAnio((short) 2024);
        request.setValor(new BigDecimal("100.00"));
        request.setTipo(TipoCuota.ORDINARIA);
        request.setFechaVencimiento(LocalDate.now().plusDays(10));
        request.setEstado(EstadoCuota.PENDIENTE);

        response = CuotaResponse.builder()
                .id(1L)
                .unidadId(1L)
                .mes((short) 1)
                .anio((short) 2024)
                .valor(new BigDecimal("100.00"))
                .tipo(TipoCuota.ORDINARIA)
                .estado(EstadoCuota.PENDIENTE)
                .build();
    }

    @Test
    @DisplayName("✓ GET /api/v1/cuotas: lista cuotas")
    void shouldReturn200_whenListingCuotas() throws Exception {
        Page<CuotaResponse> page = new PageImpl<>(List.of(response));
        when(cuotaService.findAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/cuotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].tipo").value("ORDINARIA"));
    }

    @Test
    @DisplayName("✓ POST /api/v1/cuotas: crea cuota")
    void shouldReturn201_whenCreateCuota() throws Exception {
        when(cuotaService.create(any(CuotaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/cuotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }
}
