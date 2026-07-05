package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.CategoriaRequest;
import com.condominio.condominio_api.dto.response.CategoriaResponse;
import com.condominio.condominio_api.entity.Categoria;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaResponse toResponse(Categoria entity);

    @Mapping(target = "id", ignore = true)
    Categoria toEntity(CategoriaRequest request);

    List<CategoriaResponse> toResponseList(List<Categoria> entities);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CategoriaRequest request, @MappingTarget Categoria entity);
}
