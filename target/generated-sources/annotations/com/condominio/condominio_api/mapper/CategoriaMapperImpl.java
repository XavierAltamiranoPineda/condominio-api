package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.CategoriaRequest;
import com.condominio.condominio_api.dto.response.CategoriaResponse;
import com.condominio.condominio_api.entity.Categoria;
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
public class CategoriaMapperImpl implements CategoriaMapper {

    @Override
    public CategoriaResponse toResponse(Categoria entity) {
        if ( entity == null ) {
            return null;
        }

        CategoriaResponse.CategoriaResponseBuilder categoriaResponse = CategoriaResponse.builder();

        categoriaResponse.id( entity.getId() );
        categoriaResponse.nombre( entity.getNombre() );

        return categoriaResponse.build();
    }

    @Override
    public Categoria toEntity(CategoriaRequest request) {
        if ( request == null ) {
            return null;
        }

        Categoria categoria = new Categoria();

        categoria.setNombre( request.getNombre() );

        return categoria;
    }

    @Override
    public List<CategoriaResponse> toResponseList(List<Categoria> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CategoriaResponse> list = new ArrayList<CategoriaResponse>( entities.size() );
        for ( Categoria categoria : entities ) {
            list.add( toResponse( categoria ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(CategoriaRequest request, Categoria entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getNombre() != null ) {
            entity.setNombre( request.getNombre() );
        }
    }
}
