package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComunicadoLecturaRequest {

    @NotNull(message = "El id del comunicado es obligatorio")
    private Long comunicadoId;

    @NotNull(message = "El id de la persona es obligatorio")
    private Long personaId;
}
