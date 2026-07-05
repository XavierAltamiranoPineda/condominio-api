package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.VisitantePreautorizadoRequest;
import com.condominio.condominio_api.dto.response.VisitantePreautorizadoResponse;
import com.condominio.condominio_api.entity.VisitantePreautorizado;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitantePreautorizadoMapper {

    @Mapping(source = "visitante.id", target = "visitanteId")
    @Mapping(source = "visitante.nombre", target = "visitanteNombre")
    @Mapping(source = "visitante.cedula", target = "visitanteCedula")
    @Mapping(source = "unidad.id", target = "unidadId")
    @Mapping(source = "unidad.numero", target = "unidadNumero")
    @Mapping(source = "autorizadoPor.id", target = "autorizadoPorId")
    @Mapping(source = "autorizadoPor.nombres", target = "autorizadoPorNombres")
    @Mapping(source = "autorizadoPor.apellidos", target = "autorizadoPorApellidos")
    VisitantePreautorizadoResponse toResponse(VisitantePreautorizado visitantePreautorizado);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visitante", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "autorizadoPor", ignore = true)
    @Mapping(target = "fechaAutorizada", ignore = true)
    VisitantePreautorizado toEntity(VisitantePreautorizadoRequest request);

    List<VisitantePreautorizadoResponse> toResponseList(List<VisitantePreautorizado> visitantesPreautorizados);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visitante", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "autorizadoPor", ignore = true)
    @Mapping(target = "fechaAutorizada", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(VisitantePreautorizadoRequest request, @MappingTarget VisitantePreautorizado visitantePreautorizado);
}
