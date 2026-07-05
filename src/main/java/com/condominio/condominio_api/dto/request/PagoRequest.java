package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class PagoRequest {

    @NotNull(message = "El id de la cuota es obligatorio")
    private Long cuotaId;

    @NotNull(message = "El id del estado es obligatorio")
    private Long estadoId;

    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    private BigDecimal valor;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String metodo;

    @Size(max = 100, message = "Máximo 100 caracteres")
    private String referencia;
    
    private OffsetDateTime fecha;
}
