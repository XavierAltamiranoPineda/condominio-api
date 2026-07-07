package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ReciboRequest;
import com.condominio.condominio_api.dto.response.ReciboResponse;
import com.condominio.condominio_api.entity.Archivo;
import com.condominio.condominio_api.entity.Pago;
import com.condominio.condominio_api.entity.Recibo;
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
public class ReciboMapperImpl implements ReciboMapper {

    @Override
    public ReciboResponse toResponse(Recibo recibo) {
        if ( recibo == null ) {
            return null;
        }

        ReciboResponse.ReciboResponseBuilder reciboResponse = ReciboResponse.builder();

        reciboResponse.pagoId( reciboPagoId( recibo ) );
        reciboResponse.pagoMetodo( reciboPagoMetodo( recibo ) );
        reciboResponse.archivoId( reciboArchivoId( recibo ) );
        reciboResponse.archivoNombre( reciboArchivoNombre( recibo ) );
        reciboResponse.id( recibo.getId() );
        reciboResponse.numero( recibo.getNumero() );

        return reciboResponse.build();
    }

    @Override
    public Recibo toEntity(ReciboRequest request) {
        if ( request == null ) {
            return null;
        }

        Recibo recibo = new Recibo();

        recibo.setNumero( request.getNumero() );

        return recibo;
    }

    @Override
    public List<ReciboResponse> toResponseList(List<Recibo> list) {
        if ( list == null ) {
            return null;
        }

        List<ReciboResponse> list1 = new ArrayList<ReciboResponse>( list.size() );
        for ( Recibo recibo : list ) {
            list1.add( toResponse( recibo ) );
        }

        return list1;
    }

    @Override
    public void updateEntityFromRequest(ReciboRequest request, Recibo recibo) {
        if ( request == null ) {
            return;
        }

        if ( request.getNumero() != null ) {
            recibo.setNumero( request.getNumero() );
        }
    }

    private Long reciboPagoId(Recibo recibo) {
        Pago pago = recibo.getPago();
        if ( pago == null ) {
            return null;
        }
        return pago.getId();
    }

    private String reciboPagoMetodo(Recibo recibo) {
        Pago pago = recibo.getPago();
        if ( pago == null ) {
            return null;
        }
        return pago.getMetodo();
    }

    private Long reciboArchivoId(Recibo recibo) {
        Archivo archivo = recibo.getArchivo();
        if ( archivo == null ) {
            return null;
        }
        return archivo.getId();
    }

    private String reciboArchivoNombre(Recibo recibo) {
        Archivo archivo = recibo.getArchivo();
        if ( archivo == null ) {
            return null;
        }
        return archivo.getNombre();
    }
}
