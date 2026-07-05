package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ConfiguracionRequest;
import com.condominio.condominio_api.dto.response.ConfiguracionResponse;
import com.condominio.condominio_api.entity.Configuracion;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConfiguracionMapper {

    ConfiguracionResponse toResponse(Configuracion entity);

    @Mapping(target = "id", ignore = true)
    Configuracion toEntity(ConfiguracionRequest request);

    List<ConfiguracionResponse> toResponseList(List<Configuracion> entities);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ConfiguracionRequest request, @MappingTarget Configuracion entity);
}
