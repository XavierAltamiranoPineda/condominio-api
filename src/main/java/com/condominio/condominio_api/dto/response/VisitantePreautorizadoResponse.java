package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class VisitantePreautorizadoResponse {
    private Long id;
    private Long visitanteId;
    private String visitanteNombre;
    private String visitanteCedula;
    private Long unidadId;
    private String unidadNumero;
    private Long autorizadoPorId;
    private String autorizadoPorNombres;
    private String autorizadoPorApellidos;
    private OffsetDateTime fechaAutorizada;
}
