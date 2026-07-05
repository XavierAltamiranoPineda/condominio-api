package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class HistorialTicketResponse {
    private Long id;
    private Long ticketId;
    private Long estadoId;
    private String estadoNombre;
    private Long usuarioId;
    private String usuarioEmail;
    private OffsetDateTime fecha;
    private String comentario;
}
