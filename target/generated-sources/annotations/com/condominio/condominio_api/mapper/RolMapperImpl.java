package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.RolRequest;
import com.condominio.condominio_api.dto.response.PermisoResponse;
import com.condominio.condominio_api.dto.response.RolDetalleResponse;
import com.condominio.condominio_api.dto.response.RolResponse;
import com.condominio.condominio_api.entity.Permiso;
import com.condominio.condominio_api.entity.Rol;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:12:46-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class RolMapperImpl implements RolMapper {

    @Override
    public RolResponse toResponse(Rol rol) {
        if ( rol == null ) {
            return null;
        }

        RolResponse.RolResponseBuilder rolResponse = RolResponse.builder();

        rolResponse.id( rol.getId() );
        rolResponse.nombre( rol.getNombre() );
        rolResponse.descripcion( rol.getDescripcion() );

        return rolResponse.build();
    }

    @Override
    public RolDetalleResponse toDetalleResponse(Rol rol) {
        if ( rol == null ) {
            return null;
        }

        RolDetalleResponse.RolDetalleResponseBuilder rolDetalleResponse = RolDetalleResponse.builder();

        rolDetalleResponse.id( rol.getId() );
        rolDetalleResponse.nombre( rol.getNombre() );
        rolDetalleResponse.descripcion( rol.getDescripcion() );

        rolDetalleResponse.permisos( toPermisoList(rol.getRolPermisos()) );

        return rolDetalleResponse.build();
    }

    @Override
    public Rol toEntity(RolRequest request) {
        if ( request == null ) {
            return null;
        }

        Rol rol = new Rol();

        rol.setNombre( request.getNombre() );
        rol.setDescripcion( request.getDescripcion() );

        return rol;
    }

    @Override
    public void updateFromRequest(RolRequest request, Rol rol) {
        if ( request == null ) {
            return;
        }

        if ( request.getNombre() != null ) {
            rol.setNombre( request.getNombre() );
        }
        if ( request.getDescripcion() != null ) {
            rol.setDescripcion( request.getDescripcion() );
        }
    }

    @Override
    public PermisoResponse toPermisoResponse(Permiso permiso) {
        if ( permiso == null ) {
            return null;
        }

        PermisoResponse.PermisoResponseBuilder permisoResponse = PermisoResponse.builder();

        permisoResponse.id( permiso.getId() );
        permisoResponse.nombre( permiso.getNombre() );
        permisoResponse.modulo( permiso.getModulo() );
        permisoResponse.accion( permiso.getAccion() );

        return permisoResponse.build();
    }
}
