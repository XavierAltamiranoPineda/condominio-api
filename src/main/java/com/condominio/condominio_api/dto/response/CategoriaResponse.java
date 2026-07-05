package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriaResponse {
    private Long id;
    private String nombre;
}
