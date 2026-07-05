package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ParqueaderoRequest;
import com.condominio.condominio_api.dto.response.ParqueaderoResponse;
import com.condominio.condominio_api.entity.Parqueadero;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParqueaderoMapper {

    @Mapping(source = "unidad.id", target = "unidadId")
    @Mapping(source = "unidad.numero", target = "unidadNumero")
    ParqueaderoResponse toResponse(Parqueadero entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Parqueadero toEntity(ParqueaderoRequest request);

    List<ParqueaderoResponse> toResponseList(List<Parqueadero> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ParqueaderoRequest request, @MappingTarget Parqueadero entity);
}
