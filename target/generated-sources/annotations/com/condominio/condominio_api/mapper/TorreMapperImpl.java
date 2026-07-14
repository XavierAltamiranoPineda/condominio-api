package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.TorreRequest;
import com.condominio.condominio_api.dto.response.TorreResponse;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.entity.Torre;
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
public class TorreMapperImpl implements TorreMapper {

    @Override
    public TorreResponse toResponse(Torre torre) {
        if ( torre == null ) {
            return null;
        }

        TorreResponse.TorreResponseBuilder torreResponse = TorreResponse.builder();

        torreResponse.condominioId( torreCondominioId( torre ) );
        torreResponse.condominioNombre( torreCondominioNombre( torre ) );
        torreResponse.id( torre.getId() );
        torreResponse.nombre( torre.getNombre() );

        return torreResponse.build();
    }

    @Override
    public Torre toEntity(TorreRequest request) {
        if ( request == null ) {
            return null;
        }

        Torre torre = new Torre();

        torre.setNombre( request.getNombre() );

        return torre;
    }

    @Override
    public List<TorreResponse> toResponseList(List<Torre> torres) {
        if ( torres == null ) {
            return null;
        }

        List<TorreResponse> list = new ArrayList<TorreResponse>( torres.size() );
        for ( Torre torre : torres ) {
            list.add( toResponse( torre ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(TorreRequest request, Torre torre) {
        if ( request == null ) {
            return;
        }

        if ( request.getNombre() != null ) {
            torre.setNombre( request.getNombre() );
        }
    }

    private Long torreCondominioId(Torre torre) {
        Condominio condominio = torre.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getId();
    }

    private String torreCondominioNombre(Torre torre) {
        Condominio condominio = torre.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getNombre();
    }
}
