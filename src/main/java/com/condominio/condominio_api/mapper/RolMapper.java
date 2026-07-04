package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.RolRequest;
import com.condominio.condominio_api.dto.response.PermisoResponse;
import com.condominio.condominio_api.dto.response.RolDetalleResponse;
import com.condominio.condominio_api.dto.response.RolResponse;
import com.condominio.condominio_api.entity.Permiso;
import com.condominio.condominio_api.entity.Rol;
import com.condominio.condominio_api.entity.RolPermiso;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolMapper {

    RolResponse toResponse(Rol rol);

    @Mapping(target = "permisos", expression = "java(toPermisoList(rol.getRolPermisos()))")
    RolDetalleResponse toDetalleResponse(Rol rol);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rolPermisos", ignore = true)
    @Mapping(target = "usuarioRoles", ignore = true)
    Rol toEntity(RolRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rolPermisos", ignore = true)
    @Mapping(target = "usuarioRoles", ignore = true)
    void updateFromRequest(RolRequest request, @MappingTarget Rol rol);

    default List<PermisoResponse> toPermisoList(List<RolPermiso> rolPermisos) {
        if (rolPermisos == null) return List.of();
        return rolPermisos.stream()
                .map(rp -> toPermisoResponse(rp.getPermiso()))
                .toList();
    }

    @Mapping(target = "id", source = "id")
    PermisoResponse toPermisoResponse(Permiso permiso);
}
