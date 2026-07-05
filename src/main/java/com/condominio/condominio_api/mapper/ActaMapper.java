package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ActaRequest;
import com.condominio.condominio_api.dto.response.ActaResponse;
import com.condominio.condominio_api.entity.Acta;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ArchivoMapper.class})
public interface ActaMapper {

    @Mapping(source = "asamblea.id", target = "asambleaId")
    @Mapping(source = "asamblea.fecha", target = "asambleaFecha")
    ActaResponse toResponse(Acta acta);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "asamblea", ignore = true)
    @Mapping(target = "archivos", ignore = true)
    Acta toEntity(ActaRequest request);

    List<ActaResponse> toResponseList(List<Acta> actas);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "asamblea", ignore = true)
    @Mapping(target = "archivos", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ActaRequest request, @MappingTarget Acta acta);
}
