package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.TorreRequest;
import com.condominio.condominio_api.dto.response.TorreResponse;
import com.condominio.condominio_api.entity.Torre;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TorreMapper {

    @Mapping(source = "condominio.id", target = "condominioId")
    @Mapping(source = "condominio.nombre", target = "condominioNombre")
    TorreResponse toResponse(Torre torre);

    // No mapear el condominio automáticamente desde el request, se asigna en el servicio
    @Mapping(target = "condominio", ignore = true)
    Torre toEntity(TorreRequest request);

    List<TorreResponse> toResponseList(List<Torre> torres);

    @Mapping(target = "condominio", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(TorreRequest request, @MappingTarget Torre torre);
}
