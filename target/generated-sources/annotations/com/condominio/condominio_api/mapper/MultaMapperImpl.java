package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.MultaRequest;
import com.condominio.condominio_api.dto.response.MultaResponse;
import com.condominio.condominio_api.entity.Cuota;
import com.condominio.condominio_api.entity.Multa;
import com.condominio.condominio_api.entity.Persona;
import com.condominio.condominio_api.entity.Unidad;
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
public class MultaMapperImpl implements MultaMapper {

    @Override
    public MultaResponse toResponse(Multa multa) {
        if ( multa == null ) {
            return null;
        }

        MultaResponse.MultaResponseBuilder multaResponse = MultaResponse.builder();

        multaResponse.unidadId( multaUnidadId( multa ) );
        multaResponse.unidadNumero( multaUnidadNumero( multa ) );
        multaResponse.personaId( multaPersonaId( multa ) );
        multaResponse.personaNombres( multaPersonaNombres( multa ) );
        multaResponse.personaApellidos( multaPersonaApellidos( multa ) );
        multaResponse.cuotaId( multaCuotaId( multa ) );
        multaResponse.id( multa.getId() );
        multaResponse.motivo( multa.getMotivo() );
        multaResponse.descripcion( multa.getDescripcion() );
        multaResponse.valor( multa.getValor() );
        multaResponse.fecha( multa.getFecha() );
        multaResponse.estado( multa.getEstado() );

        return multaResponse.build();
    }

    @Override
    public Multa toEntity(MultaRequest request) {
        if ( request == null ) {
            return null;
        }

        Multa multa = new Multa();

        multa.setMotivo( request.getMotivo() );
        multa.setDescripcion( request.getDescripcion() );
        multa.setValor( request.getValor() );

        return multa;
    }

    @Override
    public List<MultaResponse> toResponseList(List<Multa> multas) {
        if ( multas == null ) {
            return null;
        }

        List<MultaResponse> list = new ArrayList<MultaResponse>( multas.size() );
        for ( Multa multa : multas ) {
            list.add( toResponse( multa ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(MultaRequest request, Multa multa) {
        if ( request == null ) {
            return;
        }

        if ( request.getMotivo() != null ) {
            multa.setMotivo( request.getMotivo() );
        }
        if ( request.getDescripcion() != null ) {
            multa.setDescripcion( request.getDescripcion() );
        }
        if ( request.getValor() != null ) {
            multa.setValor( request.getValor() );
        }
    }

    private Long multaUnidadId(Multa multa) {
        Unidad unidad = multa.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getId();
    }

    private String multaUnidadNumero(Multa multa) {
        Unidad unidad = multa.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getNumero();
    }

    private Long multaPersonaId(Multa multa) {
        Persona persona = multa.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getId();
    }

    private String multaPersonaNombres(Multa multa) {
        Persona persona = multa.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getNombres();
    }

    private String multaPersonaApellidos(Multa multa) {
        Persona persona = multa.getPersona();
        if ( persona == null ) {
            return null;
        }
        return persona.getApellidos();
    }

    private Long multaCuotaId(Multa multa) {
        Cuota cuota = multa.getCuota();
        if ( cuota == null ) {
            return null;
        }
        return cuota.getId();
    }
}
