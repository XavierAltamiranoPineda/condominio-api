package com.condominio.condominio_api.dto.request;

import com.condominio.condominio_api.entity.Persona.TipoIdentificacion;
import com.condominio.condominio_api.entity.Persona.EstadoPersona;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PersonaRequest {

    @NotNull(message = "El tipo de identificación es obligatorio")
    private TipoIdentificacion tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Size(max = 30, message = "El número de identificación no puede superar los 30 caracteres")
    private String numeroIdentificacion;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
    private String apellidos;

    @Size(max = 30, message = "El teléfono no puede superar los 30 caracteres")
    private String telefono;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato de correo es inválido")
    @Size(max = 254, message = "El correo no puede superar los 254 caracteres")
    private String correo;

    private LocalDate fechaNacimiento;

    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
    private String direccion;

    private String fotoPerfil;

    private EstadoPersona estado = EstadoPersona.ACTIVO;
}
