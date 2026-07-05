package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ConvenioPagoRequest;
import com.condominio.condominio_api.dto.response.ConvenioPagoResponse;
import com.condominio.condominio_api.entity.ConvenioPago;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Unidad;
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
public class ConvenioPagoMapperImpl implements ConvenioPagoMapper {

    @Override
    public ConvenioPagoResponse toResponse(ConvenioPago convenio) {
        if ( convenio == null ) {
            return null;
        }

        ConvenioPagoResponse.ConvenioPagoResponseBuilder convenioPagoResponse = ConvenioPagoResponse.builder();

        convenioPagoResponse.personaId( convenioPersonaId( convenio ) );
        convenioPagoResponse.personaNombres( convenioPersonaNombres( convenio ) );
        convenioPagoResponse.personaApellidos( convenioPersonaApellidos( convenio ) );
        convenioPagoResponse.unidadId( convenioUnidadId( convenio ) );
        convenioPagoResponse.unidadNumero( convenioUnidadNumero( convenio ) );
        convenioPagoResponse.id( convenio.getId() );
        convenioPagoResponse.montoTotal( convenio.getMontoTotal() );
        convenioPagoResponse.numCuotas( convenio.getNumCuotas() );
        convenioPagoResponse.fechaInicio( convenio.getFechaInicio() );
        convenioPagoResponse.estado( convenio.getEstado() );

        return convenioPagoResponse.build();
    }

    @Override
    public ConvenioPago toEntity(ConvenioPagoRequest request) {
        if ( request == null ) {
            return null;
        }

        ConvenioPago convenioPago = new ConvenioPago();

        convenioPago.setMontoTotal( request.getMontoTotal() );
        convenioPago.setNumCuotas( request.getNumCuotas() );
        convenioPago.setFechaInicio( request.getFechaInicio() );

        return convenioPago;
    }

    @Override
    public List<ConvenioPagoResponse> toResponseList(List<ConvenioPago> convenios) {
        if ( convenios == null ) {
            return null;
        }

        List<ConvenioPagoResponse> list = new ArrayList<ConvenioPagoResponse>( convenios.size() );
        for ( ConvenioPago convenioPago : convenios ) {
            list.add( toResponse( convenioPago ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ConvenioPagoRequest request, ConvenioPago convenio) {
        if ( request == null ) {
            return;
        }

        if ( request.getMontoTotal() != null ) {
            convenio.setMontoTotal( request.getMontoTotal() );
        }
        if ( request.getNumCuotas() != null ) {
            convenio.setNumCuotas( request.getNumCuotas() );
        }
        if ( request.getFechaInicio() != null ) {
            convenio.setFechaInicio( request.getFechaInicio() );
        }
    }

    private Long convenioPersonaId(ConvenioPago convenioPago) {
        Persona persona = convenioPago.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getId();
    }

    private String convenioPersonaNombres(ConvenioPago convenioPago) {
        Persona persona = convenioPago.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getNombres();
    }

    private String convenioPersonaApellidos(ConvenioPago convenioPago) {
        Persona persona = convenioPago.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getApellidos();
    }

    private Long convenioUnidadId(ConvenioPago convenioPago) {
        Unidad unidad = convenioPago.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getId();
    }

    private String convenioUnidadNumero(ConvenioPago convenioPago) {
        Unidad unidad = convenioPago.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getNumero();
    }
}
