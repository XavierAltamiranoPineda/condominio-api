package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.CuotaRequest;
import com.condominio.condominio_api.dto.response.CuotaResponse;
import com.condominio.condominio_api.entity.Condominio;
import com.condominio.condominio_api.entity.Cuota;
import com.condominio.condominio_api.entity.Unidad;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-13T22:36:08-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.19 (Ubuntu)"
)
@Component
public class CuotaMapperImpl implements CuotaMapper {

    @Override
    public CuotaResponse toResponse(Cuota cuota) {
        if ( cuota == null ) {
            return null;
        }

        CuotaResponse.CuotaResponseBuilder cuotaResponse = CuotaResponse.builder();

        cuotaResponse.unidadId( cuotaUnidadId( cuota ) );
        cuotaResponse.unidadNumero( cuotaUnidadNumero( cuota ) );
        cuotaResponse.condominioNombre( cuotaUnidadCondominioNombre( cuota ) );
        cuotaResponse.id( cuota.getId() );
        cuotaResponse.mes( cuota.getMes() );
        cuotaResponse.anio( cuota.getAnio() );
        cuotaResponse.valor( cuota.getValor() );
        cuotaResponse.tipo( cuota.getTipo() );
        cuotaResponse.descripcion( cuota.getDescripcion() );
        cuotaResponse.fechaVencimiento( cuota.getFechaVencimiento() );
        cuotaResponse.estado( cuota.getEstado() );

        return cuotaResponse.build();
    }

    @Override
    public Cuota toEntity(CuotaRequest request) {
        if ( request == null ) {
            return null;
        }

        Cuota cuota = new Cuota();

        cuota.setMes( request.getMes() );
        cuota.setAnio( request.getAnio() );
        cuota.setValor( request.getValor() );
        cuota.setTipo( request.getTipo() );
        cuota.setDescripcion( request.getDescripcion() );
        cuota.setFechaVencimiento( request.getFechaVencimiento() );
        cuota.setEstado( request.getEstado() );

        return cuota;
    }

    @Override
    public List<CuotaResponse> toResponseList(List<Cuota> list) {
        if ( list == null ) {
            return null;
        }

        List<CuotaResponse> list1 = new ArrayList<CuotaResponse>( list.size() );
        for ( Cuota cuota : list ) {
            list1.add( toResponse( cuota ) );
        }

        return list1;
    }

    @Override
    public void updateEntityFromRequest(CuotaRequest request, Cuota cuota) {
        if ( request == null ) {
            return;
        }

        if ( request.getMes() != null ) {
            cuota.setMes( request.getMes() );
        }
        if ( request.getAnio() != null ) {
            cuota.setAnio( request.getAnio() );
        }
        if ( request.getValor() != null ) {
            cuota.setValor( request.getValor() );
        }
        if ( request.getTipo() != null ) {
            cuota.setTipo( request.getTipo() );
        }
        if ( request.getDescripcion() != null ) {
            cuota.setDescripcion( request.getDescripcion() );
        }
        if ( request.getFechaVencimiento() != null ) {
            cuota.setFechaVencimiento( request.getFechaVencimiento() );
        }
        if ( request.getEstado() != null ) {
            cuota.setEstado( request.getEstado() );
        }
    }

    private Long cuotaUnidadId(Cuota cuota) {
        Unidad unidad = cuota.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getId();
    }

    private String cuotaUnidadNumero(Cuota cuota) {
        Unidad unidad = cuota.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getNumero();
    }

    private String cuotaUnidadCondominioNombre(Cuota cuota) {
        Unidad unidad = cuota.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        Condominio condominio = unidad.getCondominio();
        if ( condominio == null ) {
            return null;
        }
        return condominio.getNombre();
    }
}
