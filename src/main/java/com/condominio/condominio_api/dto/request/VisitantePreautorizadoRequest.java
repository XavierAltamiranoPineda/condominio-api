package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VisitantePreautorizadoRequest {

    private Long visitanteId;
    
    // Si es un visitante nuevo y no tiene ID
    private VisitanteRequest visitanteNuevo;

    @NotNull(message = "El id de la unidad es obligatorio")
    private Long unidadId;

    @NotNull(message = "El id de la persona que autoriza es obligatorio")
    private Long autorizadoPorId;
}
