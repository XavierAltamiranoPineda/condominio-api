package com.condominio.condominio_api.dto.request;

import com.condominio.condominio_api.entity.Asamblea;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class AsambleaRequest {

    @NotNull(message = "El id del condominio es obligatorio")
    private Long condominioId;

    @NotNull(message = "La fecha es obligatoria")
    private OffsetDateTime fecha;

    @NotNull(message = "El tipo de asamblea es obligatorio")
    private Asamblea.TipoAsamblea tipo;

    private BigDecimal quorumRequerido;
}
