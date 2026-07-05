package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Multa;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class MultaResponse {
    private Long id;
    private Long unidadId;
    private String unidadNumero;
    private Long personaId;
    private String personaNombres;
    private String personaApellidos;
    private Long cuotaId;
    private String motivo;
    private String descripcion;
    private BigDecimal valor;
    private LocalDate fecha;
    private Multa.EstadoMulta estado;
}
