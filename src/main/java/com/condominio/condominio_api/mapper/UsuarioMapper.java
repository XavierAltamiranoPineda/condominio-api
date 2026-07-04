package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.UsuarioRequest;
import com.condominio.condominio_api.dto.response.UsuarioResponse;
import com.condominio.condominio_api.entity.Usuario;
import com.condominio.condominio_api.entity.UsuarioRol;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PersonaMapper.class})
public interface UsuarioMapper {

    @Mapping(target = "roles", expression = "java(toRoleNameList(usuario.getUsuarioRoles()))")
    UsuarioResponse toResponse(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "ultimoLogin", ignore = true)
    @Mapping(target = "intentosFallidos", ignore = true)
    @Mapping(target = "bloqueadoHasta", ignore = true)
    @Mapping(target = "tokenRecuperacion", ignore = true)
    @Mapping(target = "fechaExpiracionToken", ignore = true)
    @Mapping(target = "usuarioRoles", ignore = true)
    Usuario toEntity(UsuarioRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "ultimoLogin", ignore = true)
    @Mapping(target = "intentosFallidos", ignore = true)
    @Mapping(target = "bloqueadoHasta", ignore = true)
    @Mapping(target = "tokenRecuperacion", ignore = true)
    @Mapping(target = "fechaExpiracionToken", ignore = true)
    @Mapping(target = "usuarioRoles", ignore = true)
    void updateFromRequest(UsuarioRequest request, @MappingTarget Usuario usuario);

    default List<String> toRoleNameList(List<UsuarioRol> usuarioRoles) {
        if (usuarioRoles == null) return List.of();
        return usuarioRoles.stream()
                .map(ur -> ur.getRol().getNombre())
                .toList();
    }
}
