package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.PersonaUnidadRequest;
import com.condominio.condominio_api.dto.response.PersonaUnidadResponse;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.PersonaUnidad;
import com.condominio.condominio_api.entity.Unidad;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:39:50-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class PersonaUnidadMapperImpl implements PersonaUnidadMapper {

    @Override
    public PersonaUnidadResponse toResponse(PersonaUnidad personaUnidad) {
        if ( personaUnidad == null ) {
            return null;
        }

        PersonaUnidadResponse.PersonaUnidadResponseBuilder personaUnidadResponse = PersonaUnidadResponse.builder();

        personaUnidadResponse.personaId( personaUnidadPersonaId( personaUnidad ) );
        personaUnidadResponse.personaNombres( personaUnidadPersonaNombres( personaUnidad ) );
        personaUnidadResponse.personaApellidos( personaUnidadPersonaApellidos( personaUnidad ) );
        personaUnidadResponse.unidadId( personaUnidadUnidadId( personaUnidad ) );
        personaUnidadResponse.unidadNumero( personaUnidadUnidadNumero( personaUnidad ) );
        personaUnidadResponse.condominioNombre( personaUnidadUnidadCondominioNombre( personaUnidad ) );
        personaUnidadResponse.id( personaUnidad.getId() );
        personaUnidadResponse.tipo( personaUnidad.getTipo() );
        personaUnidadResponse.estado( personaUnidad.getEstado() );
        personaUnidadResponse.fechaInicio( personaUnidad.getFechaInicio() );
        personaUnidadResponse.fechaFin( personaUnidad.getFechaFin() );

        return personaUnidadResponse.build();
    }

    @Override
    public PersonaUnidad toEntity(PersonaUnidadRequest request) {
        if ( request == null ) {
            return null;
        }

        PersonaUnidad personaUnidad = new PersonaUnidad();

        personaUnidad.setTipo( request.getTipo() );
        personaUnidad.setEstado( request.getEstado() );
        personaUnidad.setFechaInicio( request.getFechaInicio() );
        personaUnidad.setFechaFin( request.getFechaFin() );

        return personaUnidad;
    }

    @Override
    public List<PersonaUnidadResponse> toResponseList(List<PersonaUnidad> list) {
        if ( list == null ) {
            return null;
        }

        List<PersonaUnidadResponse> list1 = new ArrayList<PersonaUnidadResponse>( list.size() );
        for ( PersonaUnidad personaUnidad : list ) {
            list1.add( toResponse( personaUnidad ) );
        }

        return list1;
    }

    @Override
    public void updateEntityFromRequest(PersonaUnidadRequest request, PersonaUnidad personaUnidad) {
        if ( request == null ) {
            return;
        }

        if ( request.getTipo() != null ) {
            personaUnidad.setTipo( request.getTipo() );
        }
        if ( request.getEstado() != null ) {
            personaUnidad.setEstado( request.getEstado() );
        }
        if ( request.getFechaInicio() != null ) {
            personaUnidad.setFechaInicio( request.getFechaInicio() );
        }
        if ( request.getFechaFin() != null ) {
            personaUnidad.setFechaFin( request.getFechaFin() );
        }
    }

    private Long personaUnidadPersonaId(PersonaUnidad personaUnidad) {
        Persona persona = personaUnidad.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getId();
    }

    private String personaUnidadPersonaNombres(PersonaUnidad personaUnidad) {
        Persona persona = personaUnidad.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getNombres();
    }

    private String personaUnidadPersonaApellidos(PersonaUnidad personaUnidad) {
        Persona persona = personaUnidad.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getApellidos();
    }

    private Long personaUnidadUnidadId(PersonaUnidad personaUnidad) {
        Unidad unidad = personaUnidad.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getId();
    }

    private String personaUnidadUnidadNumero(PersonaUnidad personaUnidad) {
        Unidad unidad = personaUnidad.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getNumero();
    }

    private String personaUnidadUnidadCondominioNombre(PersonaUnidad personaUnidad) {
        Unidad unidad = personaUnidad.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        Condominio condominio = unidad.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getNombre();
    }
}
