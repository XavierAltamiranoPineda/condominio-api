package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CondominioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String nombre;

    @Size(max = 255, message = "Máximo 255 caracteres")
    private String direccion;

    @Size(max = 30, message = "Máximo 30 caracteres")
    private String telefono;

    @Email(message = "El email debe ser válido")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String email;
}
