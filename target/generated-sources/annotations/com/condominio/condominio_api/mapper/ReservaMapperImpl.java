package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ReservaRequest;
import com.condominio.condominio_api.dto.response.ReservaResponse;
import com.condominio.condominio_api.entity.AreaComun;
import com.condominio.condominio_api.entity.EstadoReserva;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Reserva;
import com.condominio.condominio_api.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:28:25-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class ReservaMapperImpl implements ReservaMapper {

    @Override
    public ReservaResponse toResponse(Reserva reserva) {
        if ( reserva == null ) {
            return null;
        }

        ReservaResponse.ReservaResponseBuilder reservaResponse = ReservaResponse.builder();

        reservaResponse.areaId( reservaAreaId( reserva ) );
        reservaResponse.areaNombre( reservaAreaNombre( reserva ) );
        reservaResponse.personaId( reservaPersonaId( reserva ) );
        reservaResponse.personaNombres( reservaPersonaNombres( reserva ) );
        reservaResponse.personaApellidos( reservaPersonaApellidos( reserva ) );
        reservaResponse.estadoId( reservaEstadoId( reserva ) );
        reservaResponse.estadoNombre( reservaEstadoNombre( reserva ) );
        reservaResponse.usuarioAprobadorId( reservaUsuarioAprobadorId( reserva ) );
        reservaResponse.usuarioAprobadorUsername( reservaUsuarioAprobadorUsername( reserva ) );
        reservaResponse.id( reserva.getId() );
        reservaResponse.fecha( reserva.getFecha() );
        reservaResponse.horaInicio( reserva.getHoraInicio() );
        reservaResponse.horaFin( reserva.getHoraFin() );
        reservaResponse.fechaCreacion( reserva.getFechaCreacion() );
        reservaResponse.motivo( reserva.getMotivo() );
        reservaResponse.observaciones( reserva.getObservaciones() );
        reservaResponse.bloqueaHorario( reserva.getBloqueaHorario() );

        return reservaResponse.build();
    }

    @Override
    public Reserva toEntity(ReservaRequest request) {
        if ( request == null ) {
            return null;
        }

        Reserva reserva = new Reserva();

        reserva.setFecha( request.getFecha() );
        reserva.setHoraInicio( request.getHoraInicio() );
        reserva.setHoraFin( request.getHoraFin() );
        reserva.setMotivo( request.getMotivo() );
        reserva.setObservaciones( request.getObservaciones() );
        reserva.setBloqueaHorario( request.getBloqueaHorario() );

        return reserva;
    }

    @Override
    public List<ReservaResponse> toResponseList(List<Reserva> reservas) {
        if ( reservas == null ) {
            return null;
        }

        List<ReservaResponse> list = new ArrayList<ReservaResponse>( reservas.size() );
        for ( Reserva reserva : reservas ) {
            list.add( toResponse( reserva ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ReservaRequest request, Reserva reserva) {
        if ( request == null ) {
            return;
        }

        if ( request.getFecha() != null ) {
            reserva.setFecha( request.getFecha() );
        }
        if ( request.getHoraInicio() != null ) {
            reserva.setHoraInicio( request.getHoraInicio() );
        }
        if ( request.getHoraFin() != null ) {
            reserva.setHoraFin( request.getHoraFin() );
        }
        if ( request.getMotivo() != null ) {
            reserva.setMotivo( request.getMotivo() );
        }
        if ( request.getObservaciones() != null ) {
            reserva.setObservaciones( request.getObservaciones() );
        }
        if ( request.getBloqueaHorario() != null ) {
            reserva.setBloqueaHorario( request.getBloqueaHorario() );
        }
    }

    private Long reservaAreaId(Reserva reserva) {
        AreaComun area = reserva.getArea();
        if ( area == null ) {
            return null;
        }
        return area.getId();
    }

    private String reservaAreaNombre(Reserva reserva) {
        AreaComun area = reserva.getArea();
        if ( area == null ) {
            return null;
        }
        return area.getNombre();
    }

    private Long reservaPersonaId(Reserva reserva) {
        Persona persona = reserva.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getId();
    }

    private String reservaPersonaNombres(Reserva reserva) {
        Persona persona = reserva.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getNombres();
    }

    private String reservaPersonaApellidos(Reserva reserva) {
        Persona persona = reserva.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getApellidos();
    }

    private Long reservaEstadoId(Reserva reserva) {
        EstadoReserva estado = reserva.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getId();
    }

    private String reservaEstadoNombre(Reserva reserva) {
        EstadoReserva estado = reserva.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getNombre();
    }

    private Long reservaUsuarioAprobadorId(Reserva reserva) {
        Usuario usuarioAprobador = reserva.getUsuarioAprobador();
        if ( usuarioAprobador == null ) {
            return null;
        }
        return usuarioAprobador.getId();
    }

    private String reservaUsuarioAprobadorUsername(Reserva reserva) {
        Usuario usuarioAprobador = reserva.getUsuarioAprobador();
        if ( usuarioAprobador == null ) {
            return null;
        }
        return usuarioAprobador.getUsername();
    }
}
