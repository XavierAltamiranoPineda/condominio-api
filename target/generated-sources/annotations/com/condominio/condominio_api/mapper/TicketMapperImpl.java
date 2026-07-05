package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.TicketRequest;
import com.condominio.condominio_api.dto.response.TicketResponse;
import com.condominio.condominio_api.entity.Categoria;
import com.condominio.condominio_api.entity.EstadoTicket;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Ticket;
import com.condominio.condominio_api.entity.Unidad;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:49:51-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class TicketMapperImpl implements TicketMapper {

    @Override
    public TicketResponse toResponse(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketResponse.TicketResponseBuilder ticketResponse = TicketResponse.builder();

        ticketResponse.personaId( ticketPersonaId( ticket ) );
        ticketResponse.personaNombre( ticketPersonaNombres( ticket ) );
        ticketResponse.unidadId( ticketUnidadId( ticket ) );
        ticketResponse.unidadNombre( ticketUnidadNumero( ticket ) );
        ticketResponse.tecnicoId( ticketTecnicoId( ticket ) );
        ticketResponse.tecnicoNombre( ticketTecnicoNombres( ticket ) );
        ticketResponse.categoriaId( ticketCategoriaId( ticket ) );
        ticketResponse.categoriaNombre( ticketCategoriaNombre( ticket ) );
        ticketResponse.estadoActualId( ticketEstadoActualId( ticket ) );
        ticketResponse.estadoActualNombre( ticketEstadoActualNombre( ticket ) );
        ticketResponse.id( ticket.getId() );
        ticketResponse.titulo( ticket.getTitulo() );
        ticketResponse.descripcion( ticket.getDescripcion() );
        if ( ticket.getPrioridad() != null ) {
            ticketResponse.prioridad( ticket.getPrioridad().name() );
        }
        ticketResponse.fechaCreacion( ticket.getFechaCreacion() );
        ticketResponse.fechaCierre( ticket.getFechaCierre() );

        ticketResponse.archivosUris( toArchivoUris(ticket.getArchivos()) );

        return ticketResponse.build();
    }

    @Override
    public Ticket toEntity(TicketRequest request) {
        if ( request == null ) {
            return null;
        }

        Ticket ticket = new Ticket();

        ticket.setTitulo( request.getTitulo() );
        ticket.setDescripcion( request.getDescripcion() );
        if ( request.getPrioridad() != null ) {
            ticket.setPrioridad( Enum.valueOf( Ticket.PrioridadTicket.class, request.getPrioridad() ) );
        }

        return ticket;
    }

    @Override
    public List<TicketResponse> toResponseList(List<Ticket> tickets) {
        if ( tickets == null ) {
            return null;
        }

        List<TicketResponse> list = new ArrayList<TicketResponse>( tickets.size() );
        for ( Ticket ticket : tickets ) {
            list.add( toResponse( ticket ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(TicketRequest request, Ticket ticket) {
        if ( request == null ) {
            return;
        }

        if ( request.getTitulo() != null ) {
            ticket.setTitulo( request.getTitulo() );
        }
        if ( request.getDescripcion() != null ) {
            ticket.setDescripcion( request.getDescripcion() );
        }
        if ( request.getPrioridad() != null ) {
            ticket.setPrioridad( Enum.valueOf( Ticket.PrioridadTicket.class, request.getPrioridad() ) );
        }
    }

    private Long ticketPersonaId(Ticket ticket) {
        Persona persona = ticket.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getId();
    }

    private String ticketPersonaNombres(Ticket ticket) {
        Persona persona = ticket.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getNombres();
    }

    private Long ticketUnidadId(Ticket ticket) {
        Unidad unidad = ticket.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getId();
    }

    private String ticketUnidadNumero(Ticket ticket) {
        Unidad unidad = ticket.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getNumero();
    }

    private Long ticketTecnicoId(Ticket ticket) {
        Persona tecnico = ticket.getTecnico();
        if ( tecnico == null ) {
            return null;
        }
        return tecnico.getId();
    }

    private String ticketTecnicoNombres(Ticket ticket) {
        Persona tecnico = ticket.getTecnico();
        if ( tecnico == null ) {
            return null;
        }
        return tecnico.getNombres();
    }

    private Long ticketCategoriaId(Ticket ticket) {
        Categoria categoria = ticket.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        return categoria.getId();
    }

    private String ticketCategoriaNombre(Ticket ticket) {
        Categoria categoria = ticket.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        return categoria.getNombre();
    }

    private Long ticketEstadoActualId(Ticket ticket) {
        EstadoTicket estadoActual = ticket.getEstadoActual();
        if ( estadoActual == null ) {
            return null;
        }
        return estadoActual.getId();
    }

    private String ticketEstadoActualNombre(Ticket ticket) {
        EstadoTicket estadoActual = ticket.getEstadoActual();
        if ( estadoActual == null ) {
            return null;
        }
        return estadoActual.getNombre();
    }
}
