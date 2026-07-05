package com.condominio.condominio_api.integration.controller;

import com.condominio.condominio_api.controller.ReciboController;
import com.condominio.condominio_api.dto.request.ReciboRequest;
import com.condominio.condominio_api.dto.response.ReciboResponse;
import com.condominio.condominio_api.security.JwtTokenProvider;
import com.condominio.condominio_api.service.interfaces.ReciboService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReciboController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReciboControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReciboService reciboService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @MockBean
    private UserDetailsService userDetailsService;

    private ReciboRequest request;
    private ReciboResponse response;

    @BeforeEach
    void setUp() {
        request = new ReciboRequest();
        request.setNumero("REC-001");
        request.setPagoId(1L);

        response = ReciboResponse.builder()
                .id(1L)
                .numero("REC-001")
                .pagoId(1L)
                .build();
    }

    @Test
    @DisplayName("✓ GET /api/v1/recibos: lista recibos")
    void shouldReturn200_whenListingRecibos() throws Exception {
        Page<ReciboResponse> page = new PageImpl<>(List.of(response));
        when(reciboService.findAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/recibos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].numero").value("REC-001"));
    }

    @Test
    @DisplayName("✓ POST /api/v1/recibos: crea recibo")
    void shouldReturn201_whenCreateRecibo() throws Exception {
        when(reciboService.create(any(ReciboRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/recibos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }
}
