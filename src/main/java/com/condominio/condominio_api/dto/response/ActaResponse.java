package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class ActaResponse {
    private Long id;
    private Long asambleaId;
    private OffsetDateTime asambleaFecha;
    private String contenido;
    private List<ArchivoResponse> archivos;
}
