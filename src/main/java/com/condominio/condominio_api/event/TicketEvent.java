package com.condominio.condominio_api.event;

import com.condominio.condominio_api.entity.Ticket;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TicketEvent extends ApplicationEvent {

    private final Ticket ticket;
    private final EventType type;
    private final String mensaje;

    public TicketEvent(Object source, Ticket ticket, EventType type, String mensaje) {
        super(source);
        this.ticket = ticket;
        this.type = type;
        this.mensaje = mensaje;
    }

    public enum EventType {
        CREADO,
        ACTUALIZADO,
        CAMBIO_ESTADO,
        NUEVO_COMENTARIO
    }
}
