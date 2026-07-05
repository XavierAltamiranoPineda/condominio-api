package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ActaRequest {

    @NotNull(message = "El id de la asamblea es obligatorio")
    private Long asambleaId;

    private String contenido;

    private List<Long> archivosIds;
}
