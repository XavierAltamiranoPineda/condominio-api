package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.PagoRequest;
import com.condominio.condominio_api.dto.response.PagoResponse;
import com.condominio.condominio_api.entity.Cuota;
import com.condominio.condominio_api.entity.EstadoPago;
import com.condominio.condominio_api.entity.Pago;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-07T10:09:48-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class PagoMapperImpl implements PagoMapper {

    @Override
    public PagoResponse toResponse(Pago pago) {
        if ( pago == null ) {
            return null;
        }

        PagoResponse.PagoResponseBuilder pagoResponse = PagoResponse.builder();

        pagoResponse.cuotaId( pagoCuotaId( pago ) );
        pagoResponse.cuotaDescripcion( pagoCuotaDescripcion( pago ) );
        pagoResponse.estadoId( pagoEstadoId( pago ) );
        pagoResponse.estadoNombre( pagoEstadoNombre( pago ) );
        pagoResponse.id( pago.getId() );
        pagoResponse.fecha( pago.getFecha() );
        pagoResponse.valor( pago.getValor() );
        pagoResponse.metodo( pago.getMetodo() );
        pagoResponse.referencia( pago.getReferencia() );

        return pagoResponse.build();
    }

    @Override
    public Pago toEntity(PagoRequest request) {
        if ( request == null ) {
            return null;
        }

        Pago pago = new Pago();

        pago.setFecha( request.getFecha() );
        pago.setValor( request.getValor() );
        pago.setMetodo( request.getMetodo() );
        pago.setReferencia( request.getReferencia() );

        return pago;
    }

    @Override
    public List<PagoResponse> toResponseList(List<Pago> list) {
        if ( list == null ) {
            return null;
        }

        List<PagoResponse> list1 = new ArrayList<PagoResponse>( list.size() );
        for ( Pago pago : list ) {
            list1.add( toResponse( pago ) );
        }

        return list1;
    }

    @Override
    public void updateEntityFromRequest(PagoRequest request, Pago pago) {
        if ( request == null ) {
            return;
        }

        if ( request.getFecha() != null ) {
            pago.setFecha( request.getFecha() );
        }
        if ( request.getValor() != null ) {
            pago.setValor( request.getValor() );
        }
        if ( request.getMetodo() != null ) {
            pago.setMetodo( request.getMetodo() );
        }
        if ( request.getReferencia() != null ) {
            pago.setReferencia( request.getReferencia() );
        }
    }

    private Long pagoCuotaId(Pago pago) {
        Cuota cuota = pago.getCuota();
        if ( cuota == null ) {
            return null;
        }
        return cuota.getId();
    }

    private String pagoCuotaDescripcion(Pago pago) {
        Cuota cuota = pago.getCuota();
        if ( cuota == null ) {
            return null;
        }
        return cuota.getDescripcion();
    }

    private Long pagoEstadoId(Pago pago) {
        EstadoPago estado = pago.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getId();
    }

    private String pagoEstadoNombre(Pago pago) {
        EstadoPago estado = pago.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getNombre();
    }
}
