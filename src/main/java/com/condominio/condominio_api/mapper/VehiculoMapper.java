package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.VehiculoRequest;
import com.condominio.condominio_api.dto.response.VehiculoResponse;
import com.condominio.condominio_api.entity.Vehiculo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculoMapper {

    @Mapping(source = "unidad.id", target = "unidadId")
    @Mapping(source = "unidad.numero", target = "unidadNumero")
    @Mapping(source = "personaActual.id", target = "personaId")
    @Mapping(source = "personaActual.nombres", target = "personaNombres")
    @Mapping(source = "personaActual.apellidos", target = "personaApellidos")
    VehiculoResponse toResponse(Vehiculo entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "personaActual", ignore = true)
    Vehiculo toEntity(VehiculoRequest request);

    List<VehiculoResponse> toResponseList(List<Vehiculo> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "personaActual", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(VehiculoRequest request, @MappingTarget Vehiculo entity);
}
