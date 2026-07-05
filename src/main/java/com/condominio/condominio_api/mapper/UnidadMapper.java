package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.UnidadRequest;
import com.condominio.condominio_api.dto.response.UnidadResponse;
import com.condominio.condominio_api.entity.Unidad;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnidadMapper {

    @Mapping(source = "condominio.id", target = "condominioId")
    @Mapping(source = "condominio.nombre", target = "condominioNombre")
    @Mapping(source = "torre.id", target = "torreId")
    @Mapping(source = "torre.nombre", target = "torreNombre")
    @Mapping(source = "estado.id", target = "estadoId")
    @Mapping(source = "estado.nombre", target = "estadoNombre")
    UnidadResponse toResponse(Unidad unidad);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "condominio", ignore = true)
    @Mapping(target = "torre", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Unidad toEntity(UnidadRequest request);

    List<UnidadResponse> toResponseList(List<Unidad> unidades);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "condominio", ignore = true)
    @Mapping(target = "torre", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UnidadRequest request, @MappingTarget Unidad unidad);
}
