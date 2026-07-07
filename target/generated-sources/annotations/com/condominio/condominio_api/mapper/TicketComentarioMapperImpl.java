package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.TicketComentarioRequest;
import com.condominio.condominio_api.dto.response.TicketComentarioResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Ticket;
import com.condominio.condominio_api.entity.TicketComentario;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-07T10:09:48-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class TicketComentarioMapperImpl implements TicketComentarioMapper {

    @Override
    public TicketComentarioResponse toResponse(TicketComentario ticketComentario) {
        if ( ticketComentario == null ) {
            return null;
        }

        TicketComentarioResponse.TicketComentarioResponseBuilder ticketComentarioResponse = TicketComentarioResponse.builder();

        ticketComentarioResponse.ticketId( ticketComentarioTicketId( ticketComentario ) );
        ticketComentarioResponse.personaId( ticketComentarioPersonaId( ticketComentario ) );
        ticketComentarioResponse.personaNombre( ticketComentarioPersonaNombres( ticketComentario ) );
        ticketComentarioResponse.id( ticketComentario.getId() );
        ticketComentarioResponse.comentario( ticketComentario.getComentario() );
        ticketComentarioResponse.fecha( ticketComentario.getFecha() );

        return ticketComentarioResponse.build();
    }

    @Override
    public TicketComentario toEntity(TicketComentarioRequest request) {
        if ( request == null ) {
            return null;
        }

        TicketComentario ticketComentario = new TicketComentario();

        ticketComentario.setComentario( request.getComentario() );

        return ticketComentario;
    }

    @Override
    public List<TicketComentarioResponse> toResponseList(List<TicketComentario> comentarios) {
        if ( comentarios == null ) {
            return null;
        }

        List<TicketComentarioResponse> list = new ArrayList<TicketComentarioResponse>( comentarios.size() );
        for ( TicketComentario ticketComentario : comentarios ) {
            list.add( toResponse( ticketComentario ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(TicketComentarioRequest request, TicketComentario comentario) {
        if ( request == null ) {
            return;
        }

        if ( request.getComentario() != null ) {
            comentario.setComentario( request.getComentario() );
        }
    }

    private Long ticketComentarioTicketId(TicketComentario ticketComentario) {
        Ticket ticket = ticketComentario.getTicket();
        if ( ticket == null ) {
            return null;
        }
        return ticket.getId();
    }

    private Long ticketComentarioPersonaId(TicketComentario ticketComentario) {
        Persona persona = ticketComentario.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getId();
    }

    private String ticketComentarioPersonaNombres(TicketComentario ticketComentario) {
        Persona persona = ticketComentario.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getNombres();
    }
}
