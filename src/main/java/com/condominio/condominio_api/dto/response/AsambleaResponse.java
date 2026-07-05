package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Asamblea;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class AsambleaResponse {
    private Long id;
    private Long condominioId;
    private String condominioNombre;
    private OffsetDateTime fecha;
    private Asamblea.TipoAsamblea tipo;
    private BigDecimal quorumRequerido;
    private Asamblea.EstadoAsamblea estado;
}
