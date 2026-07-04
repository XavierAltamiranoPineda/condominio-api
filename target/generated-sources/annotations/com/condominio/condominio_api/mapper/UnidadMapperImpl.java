package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.UnidadRequest;
import com.condominio.condominio_api.dto.response.UnidadResponse;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.entity.EstadoUnidad;
import com.condominio.condominio_api.entity.Torre;
import com.condominio.condominio_api.entity.Unidad;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T17:44:23-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class UnidadMapperImpl implements UnidadMapper {

    @Override
    public UnidadResponse toResponse(Unidad unidad) {
        if ( unidad == null ) {
            return null;
        }

        UnidadResponse.UnidadResponseBuilder unidadResponse = UnidadResponse.builder();

        unidadResponse.condominioId( unidadCondominioId( unidad ) );
        unidadResponse.condominioNombre( unidadCondominioNombre( unidad ) );
        unidadResponse.torreId( unidadTorreId( unidad ) );
        unidadResponse.torreNombre( unidadTorreNombre( unidad ) );
        unidadResponse.estadoId( unidadEstadoId( unidad ) );
        unidadResponse.estadoNombre( unidadEstadoNombre( unidad ) );
        unidadResponse.id( unidad.getId() );
        unidadResponse.numero( unidad.getNumero() );
        unidadResponse.piso( unidad.getPiso() );
        unidadResponse.tipo( unidad.getTipo() );
        unidadResponse.alicuota( unidad.getAlicuota() );

        return unidadResponse.build();
    }

    @Override
    public Unidad toEntity(UnidadRequest request) {
        if ( request == null ) {
            return null;
        }

        Unidad unidad = new Unidad();

        unidad.setNumero( request.getNumero() );
        unidad.setPiso( request.getPiso() );
        unidad.setTipo( request.getTipo() );
        unidad.setAlicuota( request.getAlicuota() );

        return unidad;
    }

    @Override
    public List<UnidadResponse> toResponseList(List<Unidad> unidades) {
        if ( unidades == null ) {
            return null;
        }

        List<UnidadResponse> list = new ArrayList<UnidadResponse>( unidades.size() );
        for ( Unidad unidad : unidades ) {
            list.add( toResponse( unidad ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(UnidadRequest request, Unidad unidad) {
        if ( request == null ) {
            return;
        }

        if ( request.getNumero() != null ) {
            unidad.setNumero( request.getNumero() );
        }
        if ( request.getPiso() != null ) {
            unidad.setPiso( request.getPiso() );
        }
        if ( request.getTipo() != null ) {
            unidad.setTipo( request.getTipo() );
        }
        if ( request.getAlicuota() != null ) {
            unidad.setAlicuota( request.getAlicuota() );
        }
    }

    private Long unidadCondominioId(Unidad unidad) {
        Condominio condominio = unidad.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getId();
    }

    private String unidadCondominioNombre(Unidad unidad) {
        Condominio condominio = unidad.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getNombre();
    }

    private Long unidadTorreId(Unidad unidad) {
        Torre torre = unidad.getTorre();
        if ( torre == null ) {
            return null;
        }
        return torre.getId();
    }

    private String unidadTorreNombre(Unidad unidad) {
        Torre torre = unidad.getTorre();
        if ( torre == null ) {
            return null;
        }
        return torre.getNombre();
    }

    private Long unidadEstadoId(Unidad unidad) {
        EstadoUnidad estado = unidad.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getId();
    }

    private String unidadEstadoNombre(Unidad unidad) {
        EstadoUnidad estado = unidad.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getNombre();
    }
}
