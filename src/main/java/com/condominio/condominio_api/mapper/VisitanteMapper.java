package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.VisitanteRequest;
import com.condominio.condominio_api.dto.response.VisitanteResponse;
import com.condominio.condominio_api.entity.Visitante;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitanteMapper {

    VisitanteResponse toResponse(Visitante visitante);

    @Mapping(target = "id", ignore = true)
    Visitante toEntity(VisitanteRequest request);

    List<VisitanteResponse> toResponseList(List<Visitante> visitantes);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(VisitanteRequest request, @MappingTarget Visitante visitante);
}
