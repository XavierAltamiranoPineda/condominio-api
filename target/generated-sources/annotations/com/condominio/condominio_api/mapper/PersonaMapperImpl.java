package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.PersonaRequest;
import com.condominio.condominio_api.dto.response.PersonaResponse;
import com.condominio.condominio_api.entity.Persona;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T17:30:02-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class PersonaMapperImpl implements PersonaMapper {

    @Override
    public PersonaResponse toResponse(Persona persona) {
        if ( persona == null ) {
            return null;
        }

        PersonaResponse.PersonaResponseBuilder personaResponse = PersonaResponse.builder();

        personaResponse.id( persona.getId() );
        personaResponse.tipoIdentificacion( persona.getTipoIdentificacion() );
        personaResponse.numeroIdentificacion( persona.getNumeroIdentificacion() );
        personaResponse.nombres( persona.getNombres() );
        personaResponse.apellidos( persona.getApellidos() );
        personaResponse.telefono( persona.getTelefono() );
        personaResponse.correo( persona.getCorreo() );
        personaResponse.fechaNacimiento( persona.getFechaNacimiento() );
        personaResponse.direccion( persona.getDireccion() );
        personaResponse.fotoPerfil( persona.getFotoPerfil() );
        personaResponse.estado( persona.getEstado() );

        return personaResponse.build();
    }

    @Override
    public Persona toEntity(PersonaRequest request) {
        if ( request == null ) {
            return null;
        }

        Persona persona = new Persona();

        persona.setTipoIdentificacion( request.getTipoIdentificacion() );
        persona.setNumeroIdentificacion( request.getNumeroIdentificacion() );
        persona.setNombres( request.getNombres() );
        persona.setApellidos( request.getApellidos() );
        persona.setTelefono( request.getTelefono() );
        persona.setCorreo( request.getCorreo() );
        persona.setFechaNacimiento( request.getFechaNacimiento() );
        persona.setDireccion( request.getDireccion() );
        persona.setFotoPerfil( request.getFotoPerfil() );
        persona.setEstado( request.getEstado() );

        return persona;
    }

    @Override
    public void updateFromRequest(PersonaRequest request, Persona persona) {
        if ( request == null ) {
            return;
        }

        if ( request.getTipoIdentificacion() != null ) {
            persona.setTipoIdentificacion( request.getTipoIdentificacion() );
        }
        if ( request.getNumeroIdentificacion() != null ) {
            persona.setNumeroIdentificacion( request.getNumeroIdentificacion() );
        }
        if ( request.getNombres() != null ) {
            persona.setNombres( request.getNombres() );
        }
        if ( request.getApellidos() != null ) {
            persona.setApellidos( request.getApellidos() );
        }
        if ( request.getTelefono() != null ) {
            persona.setTelefono( request.getTelefono() );
        }
        if ( request.getCorreo() != null ) {
            persona.setCorreo( request.getCorreo() );
        }
        if ( request.getFechaNacimiento() != null ) {
            persona.setFechaNacimiento( request.getFechaNacimiento() );
        }
        if ( request.getDireccion() != null ) {
            persona.setDireccion( request.getDireccion() );
        }
        if ( request.getFotoPerfil() != null ) {
            persona.setFotoPerfil( request.getFotoPerfil() );
        }
        if ( request.getEstado() != null ) {
            persona.setEstado( request.getEstado() );
        }
    }
}
