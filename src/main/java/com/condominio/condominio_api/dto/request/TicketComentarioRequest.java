package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketComentarioRequest {

    @NotNull(message = "El ID del ticket no puede ser nulo")
    private Long ticketId;

    @NotBlank(message = "El comentario no puede estar vacío")
    private String comentario;
}
