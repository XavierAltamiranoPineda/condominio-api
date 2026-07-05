package com.condominio.condominio_api.integration.controller;

import com.condominio.condominio_api.controller.PagoController;
import com.condominio.condominio_api.dto.request.PagoRequest;
import com.condominio.condominio_api.dto.response.PagoResponse;
import com.condominio.condominio_api.security.JwtTokenProvider;
import com.condominio.condominio_api.service.interfaces.PagoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PagoService pagoService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;

    private PagoRequest request;
    private PagoResponse response;

    @BeforeEach
    void setUp() {
        request = new PagoRequest();
        request.setCuotaId(1L);
        request.setEstadoId(1L);
        request.setValor(new BigDecimal("100.00"));
        request.setMetodo("TRANSFERENCIA");

        response = PagoResponse.builder()
                .id(1L)
                .cuotaId(1L)
                .estadoId(1L)
                .valor(new BigDecimal("100.00"))
                .metodo("TRANSFERENCIA")
                .build();
    }

    @Test
    @DisplayName("✓ GET /api/v1/pagos: lista pagos")
    void shouldReturn200_whenListingPagos() throws Exception {
        Page<PagoResponse> page = new PageImpl<>(List.of(response));
        when(pagoService.findAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].metodo").value("TRANSFERENCIA"));
    }

    @Test
    @DisplayName("✓ POST /api/v1/pagos: crea pago")
    void shouldReturn201_whenCreatePago() throws Exception {
        when(pagoService.create(any(PagoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }
}
