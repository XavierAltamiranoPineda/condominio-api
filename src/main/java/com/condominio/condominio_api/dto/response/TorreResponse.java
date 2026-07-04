package com.condominio.condominio_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TorreResponse {
    private Long id;
    private Long condominioId;
    private String condominioNombre;
    private String nombre;
}
