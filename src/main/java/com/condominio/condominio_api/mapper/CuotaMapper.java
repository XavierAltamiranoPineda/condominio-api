package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.CuotaRequest;
import com.condominio.condominio_api.dto.response.CuotaResponse;
import com.condominio.condominio_api.entity.Cuota;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CuotaMapper {

    @Mapping(source = "unidad.id", target = "unidadId")
    @Mapping(source = "unidad.numero", target = "unidadNumero")
    @Mapping(source = "unidad.condominio.nombre", target = "condominioNombre")
    CuotaResponse toResponse(Cuota cuota);

    @Mapping(target = "unidad", ignore = true)
    Cuota toEntity(CuotaRequest request);

    List<CuotaResponse> toResponseList(List<Cuota> list);

    @Mapping(target = "unidad", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CuotaRequest request, @MappingTarget Cuota cuota);
}
