package com.condominio.condominio_api.dto.request;

import com.condominio.condominio_api.entity.enums.TipoUnidadEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class UnidadRequest {

    @NotNull(message = "El id del condominio es obligatorio")
    private Long condominioId;

    private Long torreId; // Opcional

    @NotNull(message = "El id del estado es obligatorio")
    private Long estadoId;

    @NotBlank(message = "El número es obligatorio")
    @Size(max = 20, message = "Máximo 20 caracteres")
    private String numero;

    @Size(max = 10, message = "Máximo 10 caracteres")
    private String piso;

    @NotNull(message = "El tipo de unidad es obligatorio")
    private TipoUnidadEnum tipo;

    @DecimalMin(value = "0.0", message = "La alícuota debe ser mayor o igual a 0")
    @Digits(integer = 2, fraction = 6, message = "La alícuota debe tener un formato válido (max 2 enteros, 6 decimales)")
    private BigDecimal alicuota;
}
