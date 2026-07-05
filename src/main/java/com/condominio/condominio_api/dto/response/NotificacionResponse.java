package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Notificacion;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class NotificacionResponse {
    private Long id;
    private Long personaId;
    private String tipo;
    private String titulo;
    private String mensaje;
    private Notificacion.CanalNotificacion canal;
    private Notificacion.EstadoEnvio estadoEnvio;
    private OffsetDateTime fechaEnvio;
    private Boolean leido;
    private OffsetDateTime fechaLectura;
}
