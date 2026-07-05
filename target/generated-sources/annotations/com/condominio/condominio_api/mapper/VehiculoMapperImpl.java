package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.VehiculoRequest;
import com.condominio.condominio_api.dto.response.VehiculoResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.entity.Vehiculo;
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
public class VehiculoMapperImpl implements VehiculoMapper {

    @Override
    public VehiculoResponse toResponse(Vehiculo entity) {
        if ( entity == null ) {
            return null;
        }

        VehiculoResponse.VehiculoResponseBuilder vehiculoResponse = VehiculoResponse.builder();

        vehiculoResponse.unidadId( entityUnidadId( entity ) );
        vehiculoResponse.unidadNumero( entityUnidadNumero( entity ) );
        vehiculoResponse.personaId( entityPersonaActualId( entity ) );
        vehiculoResponse.personaNombres( entityPersonaActualNombres( entity ) );
        vehiculoResponse.personaApellidos( entityPersonaActualApellidos( entity ) );
        vehiculoResponse.id( entity.getId() );
        vehiculoResponse.tipo( entity.getTipo() );
        vehiculoResponse.placa( entity.getPlaca() );
        vehiculoResponse.marca( entity.getMarca() );
        vehiculoResponse.modelo( entity.getModelo() );
        vehiculoResponse.color( entity.getColor() );

        return vehiculoResponse.build();
    }

    @Override
    public Vehiculo toEntity(VehiculoRequest request) {
        if ( request == null ) {
            return null;
        }

        Vehiculo vehiculo = new Vehiculo();

        vehiculo.setTipo( request.getTipo() );
        vehiculo.setPlaca( request.getPlaca() );
        vehiculo.setMarca( request.getMarca() );
        vehiculo.setModelo( request.getModelo() );
        vehiculo.setColor( request.getColor() );

        return vehiculo;
    }

    @Override
    public List<VehiculoResponse> toResponseList(List<Vehiculo> entities) {
        if ( entities == null ) {
            return null;
        }

        List<VehiculoResponse> list = new ArrayList<VehiculoResponse>( entities.size() );
        for ( Vehiculo vehiculo : entities ) {
            list.add( toResponse( vehiculo ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(VehiculoRequest request, Vehiculo entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getTipo() != null ) {
            entity.setTipo( request.getTipo() );
        }
        if ( request.getPlaca() != null ) {
            entity.setPlaca( request.getPlaca() );
        }
        if ( request.getMarca() != null ) {
            entity.setMarca( request.getMarca() );
        }
        if ( request.getModelo() != null ) {
            entity.setModelo( request.getModelo() );
        }
        if ( request.getColor() != null ) {
            entity.setColor( request.getColor() );
        }
    }

    private Long entityUnidadId(Vehiculo vehiculo) {
        Unidad unidad = vehiculo.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getId();
    }

    private String entityUnidadNumero(Vehiculo vehiculo) {
        Unidad unidad = vehiculo.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getNumero();
    }

    private Long entityPersonaActualId(Vehiculo vehiculo) {
        Persona personaActual = vehiculo.getPersonaActual();
        if ( personaActual == null ) {
            return null;
        }
        return personaActual.getId();
    }

    private String entityPersonaActualNombres(Vehiculo vehiculo) {
        Persona personaActual = vehiculo.getPersonaActual();
        if ( personaActual == null ) {
            return null;
        }
        return personaActual.getNombres();
    }

    private String entityPersonaActualApellidos(Vehiculo vehiculo) {
        Persona personaActual = vehiculo.getPersonaActual();
        if ( personaActual == null ) {
            return null;
        }
        return personaActual.getApellidos();
    }
}
