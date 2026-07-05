package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Comunicado;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ComunicadoResponse {
    private Long id;
    private String titulo;
    private String mensaje;
    private OffsetDateTime fecha;
    private Long autorId;
    private String autorNombres;
    private String autorApellidos;
    private Comunicado.DestinatarioTipo destinatarioTipo;
    private Long destinatarioId;
}
