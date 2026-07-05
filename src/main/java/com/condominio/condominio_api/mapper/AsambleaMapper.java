package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.AsambleaRequest;
import com.condominio.condominio_api.dto.response.AsambleaResponse;
import com.condominio.condominio_api.entity.Asamblea;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AsambleaMapper {

    @Mapping(source = "condominio.id", target = "condominioId")
    @Mapping(source = "condominio.nombre", target = "condominioNombre")
    AsambleaResponse toResponse(Asamblea asamblea);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "condominio", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Asamblea toEntity(AsambleaRequest request);

    List<AsambleaResponse> toResponseList(List<Asamblea> asambleas);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "condominio", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AsambleaRequest request, @MappingTarget Asamblea asamblea);
}
