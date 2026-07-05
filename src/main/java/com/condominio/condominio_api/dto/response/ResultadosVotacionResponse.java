package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultadosVotacionResponse {
    private Long asambleaId;
    private long aFavor;
    private long enContra;
    private long abstencion;
    private long totalVotos;
}
