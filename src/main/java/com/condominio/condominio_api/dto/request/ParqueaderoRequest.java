package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParqueaderoRequest {

    @NotNull(message = "El id de la unidad es obligatorio")
    private Long unidadId;

    @NotBlank(message = "El número del parqueadero es obligatorio")
    private String numero;
}
