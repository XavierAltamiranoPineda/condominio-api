package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VisitanteRequest {
    
    @NotBlank(message = "El nombre del visitante es obligatorio")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres")
    private String nombre;
    
    @Size(max = 30, message = "La cédula no puede tener más de 30 caracteres")
    private String cedula;
    
    @Size(max = 30, message = "El teléfono no puede tener más de 30 caracteres")
    private String telefono;
}
