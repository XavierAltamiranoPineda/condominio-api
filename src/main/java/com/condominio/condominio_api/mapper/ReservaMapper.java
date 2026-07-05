package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ReservaRequest;
import com.condominio.condominio_api.dto.response.ReservaResponse;
import com.condominio.condominio_api.entity.Reserva;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "area.nombre", target = "areaNombre")
    @Mapping(source = "persona.id", target = "personaId")
    @Mapping(source = "persona.nombres", target = "personaNombres")
    @Mapping(source = "persona.apellidos", target = "personaApellidos")
    @Mapping(source = "estado.id", target = "estadoId")
    @Mapping(source = "estado.nombre", target = "estadoNombre")
    @Mapping(source = "usuarioAprobador.id", target = "usuarioAprobadorId")
    @Mapping(source = "usuarioAprobador.username", target = "usuarioAprobadorUsername")
    ReservaResponse toResponse(Reserva reserva);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "area", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "usuarioAprobador", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    Reserva toEntity(ReservaRequest request);

    List<ReservaResponse> toResponseList(List<Reserva> reservas);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "area", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "usuarioAprobador", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ReservaRequest request, @MappingTarget Reserva reserva);
}
