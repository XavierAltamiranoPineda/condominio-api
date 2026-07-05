package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.MultaRequest;
import com.condominio.condominio_api.dto.response.MultaResponse;
import com.condominio.condominio_api.entity.Multa;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MultaMapper {

    @Mapping(source = "unidad.id", target = "unidadId")
    @Mapping(source = "unidad.numero", target = "unidadNumero")
    @Mapping(source = "persona.id", target = "personaId")
    @Mapping(source = "persona.nombres", target = "personaNombres")
    @Mapping(source = "persona.apellidos", target = "personaApellidos")
    @Mapping(source = "cuota.id", target = "cuotaId")
    MultaResponse toResponse(Multa multa);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "cuota", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Multa toEntity(MultaRequest request);

    List<MultaResponse> toResponseList(List<Multa> multas);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "cuota", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(MultaRequest request, @MappingTarget Multa multa);
}
