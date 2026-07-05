package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.ReciboRequest;
import com.condominio.condominio_api.dto.response.ReciboResponse;
import com.condominio.condominio_api.entity.Recibo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReciboMapper {

    @Mapping(source = "pago.id", target = "pagoId")
    @Mapping(source = "pago.metodo", target = "pagoMetodo")
    @Mapping(source = "archivo.id", target = "archivoId")
    @Mapping(source = "archivo.nombre", target = "archivoNombre")
    ReciboResponse toResponse(Recibo recibo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pago", ignore = true)
    @Mapping(target = "archivo", ignore = true)
    Recibo toEntity(ReciboRequest request);

    List<ReciboResponse> toResponseList(List<Recibo> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pago", ignore = true)
    @Mapping(target = "archivo", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ReciboRequest request, @MappingTarget Recibo recibo);
}
