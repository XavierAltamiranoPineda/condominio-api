package com.condominio.condominio_api.dto.request;

import com.condominio.condominio_api.entity.Comunicado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ComunicadoRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede tener más de 150 caracteres")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotNull(message = "El id del autor es obligatorio")
    private Long autorId;

    @NotNull(message = "El tipo de destinatario es obligatorio")
    private Comunicado.DestinatarioTipo destinatarioTipo;

    private Long destinatarioId;
}
