package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.AccesoRequest;
import com.condominio.condominio_api.dto.response.AccesoResponse;
import com.condominio.condominio_api.entity.Acceso;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccesoMapper {

    @Mapping(source = "visitante.id", target = "visitanteId")
    @Mapping(source = "visitante.nombre", target = "visitanteNombre")
    @Mapping(source = "visitante.cedula", target = "visitanteCedula")
    @Mapping(source = "unidad.id", target = "unidadId")
    @Mapping(source = "unidad.numero", target = "unidadNumero")
    @Mapping(source = "guardia.id", target = "guardiaId")
    @Mapping(source = "guardia.nombres", target = "guardiaNombres")
    @Mapping(source = "preautorizacion.id", target = "preautorizacionId")
    @Mapping(source = "estado.id", target = "estadoId")
    @Mapping(source = "estado.nombre", target = "estadoNombre")
    AccesoResponse toResponse(Acceso acceso);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visitante", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "guardia", ignore = true)
    @Mapping(target = "preautorizacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "horaIngreso", ignore = true)
    @Mapping(target = "horaSalida", ignore = true)
    Acceso toEntity(AccesoRequest request);

    List<AccesoResponse> toResponseList(List<Acceso> accesos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visitante", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "guardia", ignore = true)
    @Mapping(target = "preautorizacion", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "horaIngreso", ignore = true)
    @Mapping(target = "horaSalida", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AccesoRequest request, @MappingTarget Acceso acceso);
}
