package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.VotacionRequest;
import com.condominio.condominio_api.dto.response.VotacionResponse;
import com.condominio.condominio_api.entity.Votacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VotacionMapper {

    @Mapping(source = "asamblea.id", target = "asambleaId")
    @Mapping(source = "persona.id", target = "personaId")
    @Mapping(source = "persona.nombres", target = "personaNombres")
    @Mapping(source = "persona.apellidos", target = "personaApellidos")
    VotacionResponse toResponse(Votacion votacion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "asamblea", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    Votacion toEntity(VotacionRequest request);

    List<VotacionResponse> toResponseList(List<Votacion> votaciones);
}
