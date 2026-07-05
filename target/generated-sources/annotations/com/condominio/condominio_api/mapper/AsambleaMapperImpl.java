package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.AsambleaRequest;
import com.condominio.condominio_api.dto.response.AsambleaResponse;
import com.condominio.condominio_api.entity.Asamblea;
import com.condominio.condominio_api.entity.Condominio;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:12:45-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class AsambleaMapperImpl implements AsambleaMapper {

    @Override
    public AsambleaResponse toResponse(Asamblea asamblea) {
        if ( asamblea == null ) {
            return null;
        }

        AsambleaResponse.AsambleaResponseBuilder asambleaResponse = AsambleaResponse.builder();

        asambleaResponse.condominioId( asambleaCondominioId( asamblea ) );
        asambleaResponse.condominioNombre( asambleaCondominioNombre( asamblea ) );
        asambleaResponse.id( asamblea.getId() );
        asambleaResponse.fecha( asamblea.getFecha() );
        asambleaResponse.tipo( asamblea.getTipo() );
        asambleaResponse.quorumRequerido( asamblea.getQuorumRequerido() );
        asambleaResponse.estado( asamblea.getEstado() );

        return asambleaResponse.build();
    }

    @Override
    public Asamblea toEntity(AsambleaRequest request) {
        if ( request == null ) {
            return null;
        }

        Asamblea asamblea = new Asamblea();

        asamblea.setFecha( request.getFecha() );
        asamblea.setTipo( request.getTipo() );
        asamblea.setQuorumRequerido( request.getQuorumRequerido() );

        return asamblea;
    }

    @Override
    public List<AsambleaResponse> toResponseList(List<Asamblea> asambleas) {
        if ( asambleas == null ) {
            return null;
        }

        List<AsambleaResponse> list = new ArrayList<AsambleaResponse>( asambleas.size() );
        for ( Asamblea asamblea : asambleas ) {
            list.add( toResponse( asamblea ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(AsambleaRequest request, Asamblea asamblea) {
        if ( request == null ) {
            return;
        }

        if ( request.getFecha() != null ) {
            asamblea.setFecha( request.getFecha() );
        }
        if ( request.getTipo() != null ) {
            asamblea.setTipo( request.getTipo() );
        }
        if ( request.getQuorumRequerido() != null ) {
            asamblea.setQuorumRequerido( request.getQuorumRequerido() );
        }
    }

    private Long asambleaCondominioId(Asamblea asamblea) {
        Condominio condominio = asamblea.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getId();
    }

    private String asambleaCondominioNombre(Asamblea asamblea) {
        Condominio condominio = asamblea.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getNombre();
    }
}
