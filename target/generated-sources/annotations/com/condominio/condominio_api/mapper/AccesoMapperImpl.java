package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.AccesoRequest;
import com.condominio.condominio_api.dto.response.AccesoResponse;
import com.condominio.condominio_api.entity.Acceso;
import com.condominio.condominio_api.entity.EstadoAcceso;
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
    date = "2026-07-07T10:09:48-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class AccesoMapperImpl implements AccesoMapper {

    @Override
    public AccesoResponse toResponse(Acceso acceso) {
        if ( acceso == null ) {
            return null;
        }

        AccesoResponse.AccesoResponseBuilder accesoResponse = AccesoResponse.builder();

        accesoResponse.visitanteId( accesoVisitanteId( acceso ) );
        accesoResponse.visitanteNombre( accesoVisitanteNombre( acceso ) );
        accesoResponse.visitanteCedula( accesoVisitanteCedula( acceso ) );
        accesoResponse.unidadId( accesoUnidadId( acceso ) );
        accesoResponse.unidadNumero( accesoUnidadNumero( acceso ) );
        accesoResponse.guardiaId( accesoGuardiaId( acceso ) );
        accesoResponse.guardiaNombres( accesoGuardiaNombres( acceso ) );
        accesoResponse.preautorizacionId( accesoPreautorizacionId( acceso ) );
        accesoResponse.estadoId( accesoEstadoId( acceso ) );
        accesoResponse.estadoNombre( accesoEstadoNombre( acceso ) );
        accesoResponse.id( acceso.getId() );
        accesoResponse.vehiculoId( acceso.getVehiculoId() );
        accesoResponse.horaIngreso( acceso.getHoraIngreso() );
        accesoResponse.horaSalida( acceso.getHoraSalida() );
        accesoResponse.foto( acceso.getFoto() );

        return accesoResponse.build();
    }

    @Override
    public Acceso toEntity(AccesoRequest request) {
        if ( request == null ) {
            return null;
        }

        Acceso acceso = new Acceso();

        acceso.setVehiculoId( request.getVehiculoId() );
        acceso.setFoto( request.getFoto() );

        return acceso;
    }

    @Override
    public List<AccesoResponse> toResponseList(List<Acceso> accesos) {
        if ( accesos == null ) {
            return null;
        }

        List<AccesoResponse> list = new ArrayList<AccesoResponse>( accesos.size() );
        for ( Acceso acceso : accesos ) {
            list.add( toResponse( acceso ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(AccesoRequest request, Acceso acceso) {
        if ( request == null ) {
            return;
        }

        if ( request.getVehiculoId() != null ) {
            acceso.setVehiculoId( request.getVehiculoId() );
        }
        if ( request.getFoto() != null ) {
            acceso.setFoto( request.getFoto() );
        }
    }

    private Long accesoVisitanteId(Acceso acceso) {
        Visitante visitante = acceso.getVisitante();
        if ( visitante == null ) {
            return null;
        }
        return visitante.getId();
    }

    private String accesoVisitanteNombre(Acceso acceso) {
        Visitante visitante = acceso.getVisitante();
        if ( visitante == null ) {
            return null;
        }
        return visitante.getNombre();
    }

    private String accesoVisitanteCedula(Acceso acceso) {
        Visitante visitante = acceso.getVisitante();
        if ( visitante == null ) {
            return null;
        }
        return visitante.getCedula();
    }

    private Long accesoUnidadId(Acceso acceso) {
        Unidad unidad = acceso.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getId();
    }

    private String accesoUnidadNumero(Acceso acceso) {
        Unidad unidad = acceso.getUnidad();
        if ( unidad == null ) {
            return null;
        }
        return unidad.getNumero();
    }

    private Long accesoGuardiaId(Acceso acceso) {
        Persona guardia = acceso.getGuardia();
        if ( guardia == null ) {
            return null;
        }
        return guardia.getId();
    }

    private String accesoGuardiaNombres(Acceso acceso) {
        Persona guardia = acceso.getGuardia();
        if ( guardia == null ) {
            return null;
        }
        return guardia.getNombres();
    }

    private Long accesoPreautorizacionId(Acceso acceso) {
        VisitantePreautorizado preautorizacion = acceso.getPreautorizacion();
        if ( preautorizacion == null ) {
            return null;
        }
        return preautorizacion.getId();
    }

    private Long accesoEstadoId(Acceso acceso) {
        EstadoAcceso estado = acceso.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getId();
    }

    private String accesoEstadoNombre(Acceso acceso) {
        EstadoAcceso estado = acceso.getEstado();
        if ( estado == null ) {
            return null;
        }
        return estado.getNombre();
    }
}
