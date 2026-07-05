package com.condominio.condominio_api.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class TicketComentarioResponse {
    private Long id;
    private Long ticketId;
    private Long personaId;
    private String personaNombre;
    private String comentario;
    private OffsetDateTime fecha;
}
