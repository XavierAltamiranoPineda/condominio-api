package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.AreaComunRequest;
import com.condominio.condominio_api.dto.response.AreaComunResponse;
import com.condominio.condominio_api.entity.AreaComun;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AreaComunMapper {

    @Mapping(source = "condominio.id", target = "condominioId")
    @Mapping(source = "condominio.nombre", target = "condominioNombre")
    AreaComunResponse toResponse(AreaComun areaComun);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "condominio", ignore = true)
    AreaComun toEntity(AreaComunRequest request);

    List<AreaComunResponse> toResponseList(List<AreaComun> areasComunes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "condominio", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AreaComunRequest request, @MappingTarget AreaComun areaComun);
}
