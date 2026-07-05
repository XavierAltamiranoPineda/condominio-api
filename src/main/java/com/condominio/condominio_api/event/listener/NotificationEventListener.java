package com.condominio.condominio_api.event.listener;

import com.condominio.condominio_api.event.TicketEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    // Aquí se inyectaría EmailService, PushNotificationService, etc.

    @Async
    @EventListener
    public void onTicketEvent(TicketEvent event) {
        // Base para enviar notificaciones push o emails.
        log.info("Notificación disparada - Evento: {}, Ticket ID: {}, Mensaje: {}", 
                event.getType(), 
                event.getTicket().getId(), 
                event.getMensaje());
        
        switch (event.getType()) {
            case CREADO -> enviarNotificacionCreacion(event);
            case CAMBIO_ESTADO -> enviarNotificacionCambioEstado(event);
            case NUEVO_COMENTARIO -> enviarNotificacionNuevoComentario(event);
            default -> log.debug("Evento no maneja notificación específica");
        }
    }

    private void enviarNotificacionCreacion(TicketEvent event) {
        // Lógica de notificar al usuario (resident/propietario) y al admin del condominio
        log.info("Simulando envío de correo/push: Ticket Creado -> {}", event.getMensaje());
    }

    private void enviarNotificacionCambioEstado(TicketEvent event) {
        // Lógica de notificar al usuario que reportó el ticket
        log.info("Simulando envío de correo/push: Cambio Estado -> {}", event.getMensaje());
    }

    private void enviarNotificacionNuevoComentario(TicketEvent event) {
        // Notificar a todos los involucrados (menos al autor del comentario)
        log.info("Simulando envío de correo/push: Nuevo Comentario -> {}", event.getMensaje());
    }
}
