package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.NotificacionRequest;
import com.condominio.condominio_api.dto.response.NotificacionResponse;
import com.condominio.condominio_api.entity.Notificacion;
import com.condominio.condominio_api.entity.Persona;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-13T22:36:07-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.19 (Ubuntu)"
)
@Component
public class NotificacionMapperImpl implements NotificacionMapper {

    @Override
    public NotificacionResponse toResponse(Notificacion entity) {
        if ( entity == null ) {
            return null;
        }

        NotificacionResponse.NotificacionResponseBuilder notificacionResponse = NotificacionResponse.builder();

        notificacionResponse.personaId( entityPersonaId( entity ) );
        notificacionResponse.id( entity.getId() );
        notificacionResponse.tipo( entity.getTipo() );
        notificacionResponse.titulo( entity.getTitulo() );
        notificacionResponse.mensaje( entity.getMensaje() );
        notificacionResponse.canal( entity.getCanal() );
        notificacionResponse.estadoEnvio( entity.getEstadoEnvio() );
        notificacionResponse.fechaEnvio( entity.getFechaEnvio() );
        notificacionResponse.leido( entity.getLeido() );
        notificacionResponse.fechaLectura( entity.getFechaLectura() );

        return notificacionResponse.build();
    }

    @Override
    public Notificacion toEntity(NotificacionRequest request) {
        if ( request == null ) {
            return null;
        }

        Notificacion notificacion = new Notificacion();

        notificacion.setTipo( request.getTipo() );
        notificacion.setTitulo( request.getTitulo() );
        notificacion.setMensaje( request.getMensaje() );
        notificacion.setCanal( request.getCanal() );

        return notificacion;
    }

    @Override
    public List<NotificacionResponse> toResponseList(List<Notificacion> entities) {
        if ( entities == null ) {
            return null;
        }

        List<NotificacionResponse> list = new ArrayList<NotificacionResponse>( entities.size() );
        for ( Notificacion notificacion : entities ) {
            list.add( toResponse( notificacion ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(NotificacionRequest request, Notificacion entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getTipo() != null ) {
            entity.setTipo( request.getTipo() );
        }
        if ( request.getTitulo() != null ) {
            entity.setTitulo( request.getTitulo() );
        }
        if ( request.getMensaje() != null ) {
            entity.setMensaje( request.getMensaje() );
        }
        if ( request.getCanal() != null ) {
            entity.setCanal( request.getCanal() );
        }
    }

    private Long entityPersonaId(Notificacion notificacion) {
        Persona persona = notificacion.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getId();
    }
}
