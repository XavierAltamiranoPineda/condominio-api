package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.VisitanteRequest;
import com.condominio.condominio_api.dto.response.VisitanteResponse;
import com.condominio.condominio_api.entity.Visitante;
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
public class VisitanteMapperImpl implements VisitanteMapper {

    @Override
    public VisitanteResponse toResponse(Visitante visitante) {
        if ( visitante == null ) {
            return null;
        }

        VisitanteResponse.VisitanteResponseBuilder visitanteResponse = VisitanteResponse.builder();

        visitanteResponse.id( visitante.getId() );
        visitanteResponse.nombre( visitante.getNombre() );
        visitanteResponse.cedula( visitante.getCedula() );
        visitanteResponse.telefono( visitante.getTelefono() );

        return visitanteResponse.build();
    }

    @Override
    public Visitante toEntity(VisitanteRequest request) {
        if ( request == null ) {
            return null;
        }

        Visitante visitante = new Visitante();

        visitante.setNombre( request.getNombre() );
        visitante.setCedula( request.getCedula() );
        visitante.setTelefono( request.getTelefono() );

        return visitante;
    }

    @Override
    public List<VisitanteResponse> toResponseList(List<Visitante> visitantes) {
        if ( visitantes == null ) {
            return null;
        }

        List<VisitanteResponse> list = new ArrayList<VisitanteResponse>( visitantes.size() );
        for ( Visitante visitante : visitantes ) {
            list.add( toResponse( visitante ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(VisitanteRequest request, Visitante visitante) {
        if ( request == null ) {
            return;
        }

        if ( request.getNombre() != null ) {
            visitante.setNombre( request.getNombre() );
        }
        if ( request.getCedula() != null ) {
            visitante.setCedula( request.getCedula() );
        }
        if ( request.getTelefono() != null ) {
            visitante.setTelefono( request.getTelefono() );
        }
    }
}
