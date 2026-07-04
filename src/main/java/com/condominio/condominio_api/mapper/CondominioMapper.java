package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.CondominioRequest;
import com.condominio.condominio_api.dto.response.CondominioResponse;
import com.condominio.condominio_api.entity.Condominio;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CondominioMapper {

    CondominioResponse toResponse(Condominio condominio);

    Condominio toEntity(CondominioRequest request);

    List<CondominioResponse> toResponseList(List<Condominio> condominios);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CondominioRequest request, @MappingTarget Condominio condominio);
}
