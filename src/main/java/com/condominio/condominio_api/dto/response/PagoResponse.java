package com.condominio.condominio_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {
    private Long id;
    private Long cuotaId;
    private String cuotaDescripcion;
    private Long estadoId;
    private String estadoNombre;
    private OffsetDateTime fecha;
    private BigDecimal valor;
    private String metodo;
    private String referencia;
}
