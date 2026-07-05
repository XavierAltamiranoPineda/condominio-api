package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReciboRequest {

    @NotBlank(message = "El número de recibo es obligatorio")
    @Size(max = 30, message = "Máximo 30 caracteres")
    private String numero;

    @NotNull(message = "El id de pago es obligatorio")
    private Long pagoId;

    private Long archivoId;
}
