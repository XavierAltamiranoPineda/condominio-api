package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.response.HistorialTicketResponse;
import com.condominio.condominio_api.entity.EstadoTicket;
import com.condominio.condominio_api.entity.HistorialTicket;
import com.condominio.condominio_api.entity.Ticket;
import com.condominio.condominio_api.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:28:24-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class HistorialTicketMapperImpl implements HistorialTicketMapper {

    @Override
    public HistorialTicketResponse toResponse(HistorialTicket historialTicket) {
        if ( historialTicket == null ) {
            return null;
        }

        HistorialTicketResponse.HistorialTicketResponseBuilder historialTicketResponse = HistorialTicketResponse.builder();

        historialTicketResponse.ticketId( historialTicketTicketId( historialTicket ) );
        historialTicketResponse.estadoId( historialTicketEstadoId( historialTicket ) );
        historialTicketResponse.estadoNombre( historialTicketEstadoNombre( historialTicket ) );
        historialTicketResponse.usuarioId( historialTicketUsuarioId( historialTicket ) );
        historialTicketResponse.usuarioEmail( historialTicketUsuarioUsername( historialTicket ) );
        historialTicketResponse.id( historialTicket.getId() );
        historialTicketResponse.fecha( historialTicket.getFecha() );
        historialTicketResponse.comentario( historialTicket.getComentario() );

        return historialTicketResponse.build();
    }

    @Override
    public List<HistorialTicketResponse> toResponseList(List<HistorialTicket> historiales) {
        if ( historiales == null ) {
            return null;
        }

        List<HistorialTicketResponse> list = new ArrayList<HistorialTicketResponse>( historiales.size() );
        for ( HistorialTicket historialTicket : historiales ) {
            list.add( toResponse( historialTicket ) );
        }

        return list;
    }

    private Long historialTicketTicketId(HistorialTicket historialTicket) {
        Ticket ticket = historialTicket.getTicket();
        if ( ticket == null ) {
            return null;
        }
        return ticket.getId();
    }

    private Long historialTicketEstadoId(HistorialTicket historialTicket) {
        EstadoTicket estado = historialTicket.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getId();
    }

    private String historialTicketEstadoNombre(HistorialTicket historialTicket) {
        EstadoTicket estado = historialTicket.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getNombre();
    }

    private Long historialTicketUsuarioId(HistorialTicket historialTicket) {
        Usuario usuario = historialTicket.getUsuario();
        if ( usuario == null ) {
            return null;
        }
        return usuario.getId();
    }

    private String historialTicketUsuarioUsername(HistorialTicket historialTicket) {
        Usuario usuario = historialTicket.getUsuario();
        if ( usuario == null ) {
            return null;
        }
        return usuario.getUsername();
    }
}
