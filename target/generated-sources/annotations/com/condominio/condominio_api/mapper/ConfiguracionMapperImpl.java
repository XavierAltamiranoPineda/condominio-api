package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ConfiguracionRequest;
import com.condominio.condominio_api.dto.response.ConfiguracionResponse;
import com.condominio.condominio_api.entity.Configuracion;
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
public class ConfiguracionMapperImpl implements ConfiguracionMapper {

    @Override
    public ConfiguracionResponse toResponse(Configuracion entity) {
        if ( entity == null ) {
            return null;
        }

        ConfiguracionResponse.ConfiguracionResponseBuilder configuracionResponse = ConfiguracionResponse.builder();

        configuracionResponse.id( entity.getId() );
        configuracionResponse.clave( entity.getClave() );
        configuracionResponse.valor( entity.getValor() );

        return configuracionResponse.build();
    }

    @Override
    public Configuracion toEntity(ConfiguracionRequest request) {
        if ( request == null ) {
            return null;
        }

        Configuracion configuracion = new Configuracion();

        configuracion.setClave( request.getClave() );
        configuracion.setValor( request.getValor() );

        return configuracion;
    }

    @Override
    public List<ConfiguracionResponse> toResponseList(List<Configuracion> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ConfiguracionResponse> list = new ArrayList<ConfiguracionResponse>( entities.size() );
        for ( Configuracion configuracion : entities ) {
            list.add( toResponse( configuracion ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ConfiguracionRequest request, Configuracion entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getClave() != null ) {
            entity.setClave( request.getClave() );
        }
        if ( request.getValor() != null ) {
            entity.setValor( request.getValor() );
        }
    }
}
