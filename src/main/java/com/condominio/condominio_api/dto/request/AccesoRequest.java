package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccesoRequest {

    private Long visitanteId;
    
    // Si el guardia registra un visitante nuevo al vuelo
    private VisitanteRequest visitanteNuevo;

    @NotNull(message = "El id de la unidad es obligatorio")
    private Long unidadId;

    @NotNull(message = "El id del guardia es obligatorio")
    private Long guardiaId;

    private Long preautorizacionId;

    private Long vehiculoId;

    private String foto;
}
