package com.condominio.condominio_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketRequest {

    @NotNull(message = "El ID de la persona no puede ser nulo")
    private Long personaId;

    @NotNull(message = "El ID de la unidad no puede ser nulo")
    private Long unidadId;

    private Long tecnicoId;
    
    private Long categoriaId;
    
    private Long estadoActualId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede exceder los 150 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "La prioridad es obligatoria")
    private String prioridad;
}
