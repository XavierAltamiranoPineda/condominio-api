package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
}
