package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ConvenioPagoRequest {

    @NotNull(message = "El id de la persona es obligatorio")
    private Long personaId;

    @NotNull(message = "El id de la unidad es obligatorio")
    private Long unidadId;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto total debe ser mayor a 0")
    private BigDecimal montoTotal;

    @NotNull(message = "El número de cuotas es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 cuota")
    private Short numCuotas;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;
}
