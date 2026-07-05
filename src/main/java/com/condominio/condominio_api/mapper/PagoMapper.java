package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.PagoRequest;
import com.condominio.condominio_api.dto.response.PagoResponse;
import com.condominio.condominio_api.entity.Pago;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PagoMapper {

    @Mapping(source = "cuota.id", target = "cuotaId")
    @Mapping(source = "cuota.descripcion", target = "cuotaDescripcion")
    @Mapping(source = "estado.id", target = "estadoId")
    @Mapping(source = "estado.nombre", target = "estadoNombre")
    PagoResponse toResponse(Pago pago);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cuota", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Pago toEntity(PagoRequest request);

    List<PagoResponse> toResponseList(List<Pago> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cuota", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(PagoRequest request, @MappingTarget Pago pago);
}
