package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.VotacionRequest;
import com.condominio.condominio_api.dto.response.VotacionResponse;
import com.condominio.condominio_api.entity.Asamblea;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Votacion;
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
public class VotacionMapperImpl implements VotacionMapper {

    @Override
    public VotacionResponse toResponse(Votacion votacion) {
        if ( votacion == null ) {
            return null;
        }

        VotacionResponse.VotacionResponseBuilder votacionResponse = VotacionResponse.builder();

        votacionResponse.asambleaId( votacionAsambleaId( votacion ) );
        votacionResponse.personaId( votacionPersonaId( votacion ) );
        votacionResponse.personaNombres( votacionPersonaNombres( votacion ) );
        votacionResponse.personaApellidos( votacionPersonaApellidos( votacion ) );
        votacionResponse.id( votacion.getId() );
        votacionResponse.opcion( votacion.getOpcion() );
        votacionResponse.fecha( votacion.getFecha() );

        return votacionResponse.build();
    }

    @Override
    public Votacion toEntity(VotacionRequest request) {
        if ( request == null ) {
            return null;
        }

        Votacion votacion = new Votacion();

        votacion.setOpcion( request.getOpcion() );

        return votacion;
    }

    @Override
    public List<VotacionResponse> toResponseList(List<Votacion> votaciones) {
        if ( votaciones == null ) {
            return null;
        }

        List<VotacionResponse> list = new ArrayList<VotacionResponse>( votaciones.size() );
        for ( Votacion votacion : votaciones ) {
            list.add( toResponse( votacion ) );
        }

        return list;
    }

    private Long votacionAsambleaId(Votacion votacion) {
        Asamblea asamblea = votacion.getAsamblea();
        if ( asamblea == null ) {
            return null;
        }
        return asamblea.getId();
    }

    private Long votacionPersonaId(Votacion votacion) {
        Persona persona = votacion.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getId();
    }

    private String votacionPersonaNombres(Votacion votacion) {
        Persona persona = votacion.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getNombres();
    }

    private String votacionPersonaApellidos(Votacion votacion) {
        Persona persona = votacion.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getApellidos();
    }
}
