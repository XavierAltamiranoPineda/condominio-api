package com.condominio.condominio_api.dto.request;

import com.condominio.condominio_api.entity.Cuota.EstadoCuota;
import com.condominio.condominio_api.entity.Cuota.TipoCuota;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CuotaRequest {

    @NotNull(message = "El id de unidad es obligatorio")
    private Long unidadId;

    @NotNull(message = "El mes es obligatorio")
    @Min(value = 1, message = "El mes debe ser mayor o igual a 1")
    @Max(value = 12, message = "El mes debe ser menor o igual a 12")
    private Short mes;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 2000, message = "Año inválido")
    private Short anio;

    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El valor no puede ser negativo")
    private BigDecimal valor;

    @NotNull(message = "El tipo es obligatorio")
    private TipoCuota tipo;

    private String descripcion;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;

    @NotNull(message = "El estado es obligatorio")
    private EstadoCuota estado;
}
