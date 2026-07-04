package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.CondominioRequest;
import com.condominio.condominio_api.dto.response.CondominioResponse;
import com.condominio.condominio_api.entity.Condominio;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T17:30:02-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class CondominioMapperImpl implements CondominioMapper {

    @Override
    public CondominioResponse toResponse(Condominio condominio) {
        if ( condominio == null ) {
            return null;
        }

        CondominioResponse.CondominioResponseBuilder condominioResponse = CondominioResponse.builder();

        condominioResponse.id( condominio.getId() );
        condominioResponse.nombre( condominio.getNombre() );
        condominioResponse.direccion( condominio.getDireccion() );
        condominioResponse.telefono( condominio.getTelefono() );
        condominioResponse.email( condominio.getEmail() );

        return condominioResponse.build();
    }

    @Override
    public Condominio toEntity(CondominioRequest request) {
        if ( request == null ) {
            return null;
        }

        Condominio condominio = new Condominio();

        condominio.setNombre( request.getNombre() );
        condominio.setDireccion( request.getDireccion() );
        condominio.setTelefono( request.getTelefono() );
        condominio.setEmail( request.getEmail() );

        return condominio;
    }

    @Override
    public List<CondominioResponse> toResponseList(List<Condominio> condominios) {
        if ( condominios == null ) {
            return null;
        }

        List<CondominioResponse> list = new ArrayList<CondominioResponse>( condominios.size() );
        for ( Condominio condominio : condominios ) {
            list.add( toResponse( condominio ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(CondominioRequest request, Condominio condominio) {
        if ( request == null ) {
            return;
        }

        if ( request.getNombre() != null ) {
            condominio.setNombre( request.getNombre() );
        }
        if ( request.getDireccion() != null ) {
            condominio.setDireccion( request.getDireccion() );
        }
        if ( request.getTelefono() != null ) {
            condominio.setTelefono( request.getTelefono() );
        }
        if ( request.getEmail() != null ) {
            condominio.setEmail( request.getEmail() );
        }
    }
}
