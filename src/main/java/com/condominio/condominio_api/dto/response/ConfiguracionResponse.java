package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfiguracionResponse {
    private Long id;
    private String clave;
    private String valor;
}
