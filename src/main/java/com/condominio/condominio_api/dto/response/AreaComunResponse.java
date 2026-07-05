package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AreaComunResponse {
    private Long id;
    private Long condominioId;
    private String condominioNombre;
    private String nombre;
    private String descripcion;
    private Integer capacidad;
}
