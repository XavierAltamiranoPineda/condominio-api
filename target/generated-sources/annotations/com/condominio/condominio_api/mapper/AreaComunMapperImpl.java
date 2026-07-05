package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.AreaComunRequest;
import com.condominio.condominio_api.dto.response.AreaComunResponse;
import com.condominio.condominio_api.entity.AreaComun;
import com.condominio.condominio_api.entity.Condominio;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T21:32:31-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class AreaComunMapperImpl implements AreaComunMapper {

    @Override
    public AreaComunResponse toResponse(AreaComun areaComun) {
        if ( areaComun == null ) {
            return null;
        }

        AreaComunResponse.AreaComunResponseBuilder areaComunResponse = AreaComunResponse.builder();

        areaComunResponse.condominioId( areaComunCondominioId( areaComun ) );
        areaComunResponse.condominioNombre( areaComunCondominioNombre( areaComun ) );
        areaComunResponse.id( areaComun.getId() );
        areaComunResponse.nombre( areaComun.getNombre() );
        areaComunResponse.descripcion( areaComun.getDescripcion() );
        areaComunResponse.capacidad( areaComun.getCapacidad() );

        return areaComunResponse.build();
    }

    @Override
    public AreaComun toEntity(AreaComunRequest request) {
        if ( request == null ) {
            return null;
        }

        AreaComun areaComun = new AreaComun();

        areaComun.setNombre( request.getNombre() );
        areaComun.setDescripcion( request.getDescripcion() );
        areaComun.setCapacidad( request.getCapacidad() );

        return areaComun;
    }

    @Override
    public List<AreaComunResponse> toResponseList(List<AreaComun> areasComunes) {
        if ( areasComunes == null ) {
            return null;
        }

        List<AreaComunResponse> list = new ArrayList<AreaComunResponse>( areasComunes.size() );
        for ( AreaComun areaComun : areasComunes ) {
            list.add( toResponse( areaComun ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(AreaComunRequest request, AreaComun areaComun) {
        if ( request == null ) {
            return;
        }

        if ( request.getNombre() != null ) {
            areaComun.setNombre( request.getNombre() );
        }
        if ( request.getDescripcion() != null ) {
            areaComun.setDescripcion( request.getDescripcion() );
        }
        if ( request.getCapacidad() != null ) {
            areaComun.setCapacidad( request.getCapacidad() );
        }
    }

    private Long areaComunCondominioId(AreaComun areaComun) {
        Condominio condominio = areaComun.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getId();
    }

    private String areaComunCondominioNombre(AreaComun areaComun) {
        Condominio condominio = areaComun.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getNombre();
    }
}
