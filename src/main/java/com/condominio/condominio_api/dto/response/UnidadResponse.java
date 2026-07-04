package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.enums.TipoUnidadEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadResponse {
    private Long id;
    private Long condominioId;
    private String condominioNombre;
    private Long torreId;
    private String torreNombre;
    private Long estadoId;
    private String estadoNombre;
    private String numero;
    private String piso;
    private TipoUnidadEnum tipo;
    private BigDecimal alicuota;
}
