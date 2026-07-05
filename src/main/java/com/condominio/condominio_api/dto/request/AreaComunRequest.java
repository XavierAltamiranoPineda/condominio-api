package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AreaComunRequest {

    @NotNull(message = "El id del condominio es obligatorio")
    private Long condominioId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    private String descripcion;

    private Integer capacidad;
}
