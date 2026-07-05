package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class TicketResponse {
    private Long id;
    private Long personaId;
    
    @com.fasterxml.jackson.annotation.JsonProperty("creadoPor")
    private String personaNombre;

    private Long unidadId;
    private String unidadNombre;
    private Long tecnicoId;
    private String tecnicoNombre;
    private Long categoriaId;
    private String categoriaNombre;
    private Long estadoActualId;
    
    @com.fasterxml.jackson.annotation.JsonProperty("estado")
    private String estadoActualNombre;

    private String titulo;
    private String descripcion;
    private String prioridad;
    private OffsetDateTime fechaCreacion;
    private OffsetDateTime fechaCierre;
    private List<String> archivosUris;
}
