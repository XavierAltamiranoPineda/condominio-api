package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ParqueaderoRequest;
import com.condominio.condominio_api.dto.response.ParqueaderoResponse;
import com.condominio.condominio_api.entity.Parqueadero;
import com.condominio.condominio_api.entity.Unidad;
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
public class ParqueaderoMapperImpl implements ParqueaderoMapper {

    @Override
    public ParqueaderoResponse toResponse(Parqueadero entity) {
        if ( entity == null ) {
            return null;
        }

        ParqueaderoResponse.ParqueaderoResponseBuilder parqueaderoResponse = ParqueaderoResponse.builder();

        parqueaderoResponse.unidadId( entityUnidadId( entity ) );
        parqueaderoResponse.unidadNumero( entityUnidadNumero( entity ) );
        parqueaderoResponse.id( entity.getId() );
        parqueaderoResponse.numero( entity.getNumero() );
        parqueaderoResponse.estado( entity.getEstado() );

        return parqueaderoResponse.build();
    }

    @Override
    public Parqueadero toEntity(ParqueaderoRequest request) {
        if ( request == null ) {
            return null;
        }

        Parqueadero parqueadero = new Parqueadero();

        parqueadero.setNumero( request.getNumero() );

        return parqueadero;
    }

    @Override
    public List<ParqueaderoResponse> toResponseList(List<Parqueadero> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ParqueaderoResponse> list = new ArrayList<ParqueaderoResponse>( entities.size() );
        for ( Parqueadero parqueadero : entities ) {
            list.add( toResponse( parqueadero ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ParqueaderoRequest request, Parqueadero entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getNumero() != null ) {
            entity.setNumero( request.getNumero() );
        }
    }

    private Long entityUnidadId(Parqueadero parqueadero) {
        Unidad unidad = parqueadero.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getId();
    }

    private String entityUnidadNumero(Parqueadero parqueadero) {
        Unidad unidad = parqueadero.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getNumero();
    }
}
