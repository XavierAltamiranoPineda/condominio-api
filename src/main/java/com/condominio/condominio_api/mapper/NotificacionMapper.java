package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.NotificacionRequest;
import com.condominio.condominio_api.dto.response.NotificacionResponse;
import com.condominio.condominio_api.entity.Notificacion;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificacionMapper {

    @Mapping(source = "persona.id", target = "personaId")
    NotificacionResponse toResponse(Notificacion entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "estadoEnvio", ignore = true)
    @Mapping(target = "fechaEnvio", ignore = true)
    @Mapping(target = "leido", ignore = true)
    @Mapping(target = "fechaLectura", ignore = true)
    Notificacion toEntity(NotificacionRequest request);

    List<NotificacionResponse> toResponseList(List<Notificacion> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "estadoEnvio", ignore = true)
    @Mapping(target = "fechaEnvio", ignore = true)
    @Mapping(target = "leido", ignore = true)
    @Mapping(target = "fechaLectura", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(NotificacionRequest request, @MappingTarget Notificacion entity);
}
