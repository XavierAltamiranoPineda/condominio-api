package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ConvenioPagoRequest;
import com.condominio.condominio_api.dto.response.ConvenioPagoResponse;
import com.condominio.condominio_api.entity.ConvenioPago;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConvenioPagoMapper {

    @Mapping(source = "persona.id", target = "personaId")
    @Mapping(source = "persona.nombres", target = "personaNombres")
    @Mapping(source = "persona.apellidos", target = "personaApellidos")
    @Mapping(source = "unidad.id", target = "unidadId")
    @Mapping(source = "unidad.numero", target = "unidadNumero")
    ConvenioPagoResponse toResponse(ConvenioPago convenio);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "estado", ignore = true)
    ConvenioPago toEntity(ConvenioPagoRequest request);

    List<ConvenioPagoResponse> toResponseList(List<ConvenioPago> convenios);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ConvenioPagoRequest request, @MappingTarget ConvenioPago convenio);
}
