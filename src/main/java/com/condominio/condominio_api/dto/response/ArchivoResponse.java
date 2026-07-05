package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ArchivoResponse {
    private Long id;
    private String nombre;
    private String ruta;
    private String tipo;
    private String mimeType;
    private Long tamano;
    private OffsetDateTime fecha;
}
