package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Votacion;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class VotacionResponse {
    private Long id;
    private Long asambleaId;
    private Long personaId;
    private String personaNombres;
    private String personaApellidos;
    private Votacion.OpcionVotacion opcion;
    private OffsetDateTime fecha;
}
