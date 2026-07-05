package com.condominio.condominio_api.dto.request;

import com.condominio.condominio_api.entity.Votacion;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VotacionRequest {

    @NotNull(message = "El id de la asamblea es obligatorio")
    private Long asambleaId;

    @NotNull(message = "El id de la persona es obligatorio")
    private Long personaId;

    @NotNull(message = "La opción de voto es obligatoria")
    private Votacion.OpcionVotacion opcion;
}
