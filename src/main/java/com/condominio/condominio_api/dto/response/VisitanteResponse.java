package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VisitanteResponse {
    private Long id;
    private String nombre;
    private String cedula;
    private String telefono;
}
