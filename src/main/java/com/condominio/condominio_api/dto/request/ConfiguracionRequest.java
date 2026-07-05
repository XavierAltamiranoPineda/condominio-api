package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfiguracionRequest {
    @NotBlank(message = "La clave es obligatoria")
    private String clave;

    @NotBlank(message = "El valor es obligatorio")
    private String valor;
}
