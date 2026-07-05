package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ActaRequest;
import com.condominio.condominio_api.dto.response.ActaResponse;
import com.condominio.condominio_api.entity.Acta;
import com.condominio.condominio_api.entity.Asamblea;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:12:45-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class ActaMapperImpl implements ActaMapper {

    @Autowired
    private ArchivoMapper archivoMapper;

    @Override
    public ActaResponse toResponse(Acta acta) {
        if ( acta == null ) {
            return null;
        }

        ActaResponse.ActaResponseBuilder actaResponse = ActaResponse.builder();

        actaResponse.asambleaId( actaAsambleaId( acta ) );
        actaResponse.asambleaFecha( actaAsambleaFecha( acta ) );
        actaResponse.id( acta.getId() );
        actaResponse.contenido( acta.getContenido() );
        actaResponse.archivos( archivoMapper.toResponseList( acta.getArchivos() ) );

        return actaResponse.build();
    }

    @Override
    public Acta toEntity(ActaRequest request) {
        if ( request == null ) {
            return null;
        }

        Acta acta = new Acta();

        acta.setContenido( request.getContenido() );

        return acta;
    }

    @Override
    public List<ActaResponse> toResponseList(List<Acta> actas) {
        if ( actas == null ) {
            return null;
        }

        List<ActaResponse> list = new ArrayList<ActaResponse>( actas.size() );
        for ( Acta acta : actas ) {
            list.add( toResponse( acta ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ActaRequest request, Acta acta) {
        if ( request == null ) {
            return;
        }

        if ( request.getContenido() != null ) {
            acta.setContenido( request.getContenido() );
        }
    }

    private Long actaAsambleaId(Acta acta) {
        Asamblea asamblea = acta.getAsamblea();
        if ( asamblea == null ) {
            return null;
        }
        return asamblea.getId();
    }

    private OffsetDateTime actaAsambleaFecha(Acta acta) {
        Asamblea asamblea = acta.getAsamblea();
        if ( asamblea == null ) {
            return null;
        }
        return asamblea.getFecha();
    }
}
