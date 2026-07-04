package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.PersonaUnidadRequest;
import com.condominio.condominio_api.dto.response.PersonaUnidadResponse;
import com.condominio.condominio_api.entity.PersonaUnidad;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonaUnidadMapper {

    @Mapping(source = "persona.id", target = "personaId")
    @Mapping(source = "persona.nombres", target = "personaNombres")
    @Mapping(source = "persona.apellidos", target = "personaApellidos")
    @Mapping(source = "unidad.id", target = "unidadId")
    @Mapping(source = "unidad.numero", target = "unidadNumero")
    @Mapping(source = "unidad.condominio.nombre", target = "condominioNombre")
    PersonaUnidadResponse toResponse(PersonaUnidad personaUnidad);

    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    PersonaUnidad toEntity(PersonaUnidadRequest request);

    List<PersonaUnidadResponse> toResponseList(List<PersonaUnidad> list);

    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(PersonaUnidadRequest request, @MappingTarget PersonaUnidad personaUnidad);
}
