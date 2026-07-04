package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.PersonaRequest;
import com.condominio.condominio_api.dto.response.PersonaResponse;
import com.condominio.condominio_api.entity.Persona;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    PersonaResponse toResponse(Persona persona);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Persona toEntity(PersonaRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    void updateFromRequest(PersonaRequest request, @MappingTarget Persona persona);
}
