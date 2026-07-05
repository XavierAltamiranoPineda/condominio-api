package com.condominio.condominio_api.dto.request;

import com.condominio.condominio_api.entity.Notificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificacionRequest {

    @NotNull(message = "El id de la persona es obligatorio")
    private Long personaId;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotNull(message = "El canal es obligatorio")
    private Notificacion.CanalNotificacion canal;
}
