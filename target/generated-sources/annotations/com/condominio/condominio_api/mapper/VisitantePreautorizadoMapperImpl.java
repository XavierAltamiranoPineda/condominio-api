package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.VisitantePreautorizadoRequest;
import com.condominio.condominio_api.dto.response.VisitantePreautorizadoResponse;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Unidad;
import com.condominio.condominio_api.entity.Visitante;
import com.condominio.condominio_api.entity.VisitantePreautorizado;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:28:24-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class VisitantePreautorizadoMapperImpl implements VisitantePreautorizadoMapper {

    @Override
    public VisitantePreautorizadoResponse toResponse(VisitantePreautorizado visitantePreautorizado) {
        if ( visitantePreautorizado == null ) {
            return null;
        }

        VisitantePreautorizadoResponse.VisitantePreautorizadoResponseBuilder visitantePreautorizadoResponse = VisitantePreautorizadoResponse.builder();

        visitantePreautorizadoResponse.visitanteId( visitantePreautorizadoVisitanteId( visitantePreautorizado ) );
        visitantePreautorizadoResponse.visitanteNombre( visitantePreautorizadoVisitanteNombre( visitantePreautorizado ) );
        visitantePreautorizadoResponse.visitanteCedula( visitantePreautorizadoVisitanteCedula( visitantePreautorizado ) );
        visitantePreautorizadoResponse.unidadId( visitantePreautorizadoUnidadId( visitantePreautorizado ) );
        visitantePreautorizadoResponse.unidadNumero( visitantePreautorizadoUnidadNumero( visitantePreautorizado ) );
        visitantePreautorizadoResponse.autorizadoPorId( visitantePreautorizadoAutorizadoPorId( visitantePreautorizado ) );
        visitantePreautorizadoResponse.autorizadoPorNombres( visitantePreautorizadoAutorizadoPorNombres( visitantePreautorizado ) );
        visitantePreautorizadoResponse.autorizadoPorApellidos( visitantePreautorizadoAutorizadoPorApellidos( visitantePreautorizado ) );
        visitantePreautorizadoResponse.id( visitantePreautorizado.getId() );
        visitantePreautorizadoResponse.fechaAutorizada( visitantePreautorizado.getFechaAutorizada() );

        return visitantePreautorizadoResponse.build();
    }

    @Override
    public VisitantePreautorizado toEntity(VisitantePreautorizadoRequest request) {
        if ( request == null ) {
            return null;
        }

        VisitantePreautorizado visitantePreautorizado = new VisitantePreautorizado();

        return visitantePreautorizado;
    }

    @Override
    public List<VisitantePreautorizadoResponse> toResponseList(List<VisitantePreautorizado> visitantesPreautorizados) {
        if ( visitantesPreautorizados == null ) {
            return null;
        }

        List<VisitantePreautorizadoResponse> list = new ArrayList<VisitantePreautorizadoResponse>( visitantesPreautorizados.size() );
        for ( VisitantePreautorizado visitantePreautorizado : visitantesPreautorizados ) {
            list.add( toResponse( visitantePreautorizado ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(VisitantePreautorizadoRequest request, VisitantePreautorizado visitantePreautorizado) {
        if ( request == null ) {
            return;
        }
    }

    private Long visitantePreautorizadoVisitanteId(VisitantePreautorizado visitantePreautorizado) {
        Visitante visitante = visitantePreautorizado.getVisitante();
        if ( visitante == null ) {
            return null;
        }
        return visitante.getId();
    }

    private String visitantePreautorizadoVisitanteNombre(VisitantePreautorizado visitantePreautorizado) {
        Visitante visitante = visitantePreautorizado.getVisitante();
        if ( visitante == null ) {
            return null;
        }
        return visitante.getNombre();
    }

    private String visitantePreautorizadoVisitanteCedula(VisitantePreautorizado visitantePreautorizado) {
        Visitante visitante = visitantePreautorizado.getVisitante();
        if ( visitante == null ) {
            return null;
        }
        return visitante.getCedula();
    }

    private Long visitantePreautorizadoUnidadId(VisitantePreautorizado visitantePreautorizado) {
        Unidad unidad = visitantePreautorizado.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getId();
    }

    private String visitantePreautorizadoUnidadNumero(VisitantePreautorizado visitantePreautorizado) {
        Unidad unidad = visitantePreautorizado.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getNumero();
    }

    private Long visitantePreautorizadoAutorizadoPorId(VisitantePreautorizado visitantePreautorizado) {
        Persona autorizadoPor = visitantePreautorizado.getAutorizadoPor();
        if ( autorizadoPor == null ) {
            return null;
        }
        return autorizadoPor.getId();
    }

    private String visitantePreautorizadoAutorizadoPorNombres(VisitantePreautorizado visitantePreautorizado) {
        Persona autorizadoPor = visitantePreautorizado.getAutorizadoPor();
        if ( autorizadoPor == null ) {
            return null;
        }
        return autorizadoPor.getNombres();
    }

    private String visitantePreautorizadoAutorizadoPorApellidos(VisitantePreautorizado visitantePreautorizado) {
        Persona autorizadoPor = visitantePreautorizado.getAutorizadoPor();
        if ( autorizadoPor == null ) {
            return null;
        }
        return autorizadoPor.getApellidos();
    }
}
