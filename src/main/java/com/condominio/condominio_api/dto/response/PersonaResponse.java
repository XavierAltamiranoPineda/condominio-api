package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Persona.TipoIdentificacion;
import com.condominio.condominio_api.entity.Persona.EstadoPersona;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaResponse {
    private Long id;
    private TipoIdentificacion tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String fotoPerfil;
    private EstadoPersona estado;
}
