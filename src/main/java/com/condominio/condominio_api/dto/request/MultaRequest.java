package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MultaRequest {

    @NotNull(message = "El id de la unidad es obligatorio")
    private Long unidadId;

    @NotNull(message = "El id de la persona es obligatorio")
    private Long personaId;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 150, message = "El motivo no puede tener más de 150 caracteres")
    private String motivo;

    private String descripcion;

    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    private BigDecimal valor;
    
    private Long cuotaId; // Opcional, si se enlaza a una cuota al crearla
}
