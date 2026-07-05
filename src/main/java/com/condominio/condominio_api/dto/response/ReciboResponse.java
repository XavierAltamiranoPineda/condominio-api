package com.condominio.condominio_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReciboResponse {
    private Long id;
    private String numero;
    private Long pagoId;
    private String pagoMetodo;
    private Long archivoId;
    private String archivoNombre;
}
