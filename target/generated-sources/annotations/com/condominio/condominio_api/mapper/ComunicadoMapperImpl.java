package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ComunicadoRequest;
import com.condominio.condominio_api.dto.response.ComunicadoResponse;
import com.condominio.condominio_api.entity.Comunicado;
import com.condominio.condominio_api.entity.Persona;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:28:25-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class ComunicadoMapperImpl implements ComunicadoMapper {

    @Override
    public ComunicadoResponse toResponse(Comunicado comunicado) {
        if ( comunicado == null ) {
            return null;
        }

        ComunicadoResponse.ComunicadoResponseBuilder comunicadoResponse = ComunicadoResponse.builder();

        comunicadoResponse.autorId( comunicadoAutorId( comunicado ) );
        comunicadoResponse.autorNombres( comunicadoAutorNombres( comunicado ) );
        comunicadoResponse.autorApellidos( comunicadoAutorApellidos( comunicado ) );
        comunicadoResponse.id( comunicado.getId() );
        comunicadoResponse.titulo( comunicado.getTitulo() );
        comunicadoResponse.mensaje( comunicado.getMensaje() );
        comunicadoResponse.fecha( comunicado.getFecha() );
        comunicadoResponse.destinatarioTipo( comunicado.getDestinatarioTipo() );
        comunicadoResponse.destinatarioId( comunicado.getDestinatarioId() );

        return comunicadoResponse.build();
    }

    @Override
    public Comunicado toEntity(ComunicadoRequest request) {
        if ( request == null ) {
            return null;
        }

        Comunicado comunicado = new Comunicado();

        comunicado.setTitulo( request.getTitulo() );
        comunicado.setMensaje( request.getMensaje() );
        comunicado.setDestinatarioTipo( request.getDestinatarioTipo() );
        comunicado.setDestinatarioId( request.getDestinatarioId() );

        return comunicado;
    }

    @Override
    public List<ComunicadoResponse> toResponseList(List<Comunicado> comunicados) {
        if ( comunicados == null ) {
            return null;
        }

        List<ComunicadoResponse> list = new ArrayList<ComunicadoResponse>( comunicados.size() );
        for ( Comunicado comunicado : comunicados ) {
            list.add( toResponse( comunicado ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ComunicadoRequest request, Comunicado comunicado) {
        if ( request == null ) {
            return;
        }

        if ( request.getTitulo() != null ) {
            comunicado.setTitulo( request.getTitulo() );
        }
        if ( request.getMensaje() != null ) {
            comunicado.setMensaje( request.getMensaje() );
        }
        if ( request.getDestinatarioTipo() != null ) {
            comunicado.setDestinatarioTipo( request.getDestinatarioTipo() );
        }
        if ( request.getDestinatarioId() != null ) {
            comunicado.setDestinatarioId( request.getDestinatarioId() );
        }
    }

    private Long comunicadoAutorId(Comunicado comunicado) {
        Persona autor = comunicado.getAutor();
        if ( autor == null ) {
            return null;
        }
        return autor.getId();
    }

    private String comunicadoAutorNombres(Comunicado comunicado) {
        Persona autor = comunicado.getAutor();
        if ( autor == null ) {
            return null;
        }
        return autor.getNombres();
    }

    private String comunicadoAutorApellidos(Comunicado comunicado) {
        Persona autor = comunicado.getAutor();
        if ( autor == null ) {
            return null;
        }
        return autor.getApellidos();
    }
}
