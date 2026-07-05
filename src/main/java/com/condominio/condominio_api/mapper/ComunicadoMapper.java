package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ComunicadoRequest;
import com.condominio.condominio_api.dto.response.ComunicadoResponse;
import com.condominio.condominio_api.entity.Comunicado;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComunicadoMapper {

    @Mapping(source = "autor.id", target = "autorId")
    @Mapping(source = "autor.nombres", target = "autorNombres")
    @Mapping(source = "autor.apellidos", target = "autorApellidos")
    ComunicadoResponse toResponse(Comunicado comunicado);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "autor", ignore = true)
    Comunicado toEntity(ComunicadoRequest request);

    List<ComunicadoResponse> toResponseList(List<Comunicado> comunicados);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "autor", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ComunicadoRequest request, @MappingTarget Comunicado comunicado);
}
