package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.PersonaRequest;
import com.condominio.condominio_api.dto.response.PersonaResponse;
import com.condominio.condominio_api.entity.Persona;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    PersonaResponse toResponse(Persona persona);

    Persona toEntity(PersonaRequest request);

    List<PersonaResponse> toResponseList(List<Persona> personas);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(PersonaRequest request, @MappingTarget Persona persona);
}
