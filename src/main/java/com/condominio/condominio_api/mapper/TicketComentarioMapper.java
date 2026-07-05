package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.TicketComentarioRequest;
import com.condominio.condominio_api.dto.response.TicketComentarioResponse;
import com.condominio.condominio_api.entity.TicketComentario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketComentarioMapper {

    @Mapping(source = "ticket.id", target = "ticketId")
    @Mapping(source = "persona.id", target = "personaId")
    @Mapping(source = "persona.nombres", target = "personaNombre")
    TicketComentarioResponse toResponse(TicketComentario ticketComentario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    TicketComentario toEntity(TicketComentarioRequest request);

    List<TicketComentarioResponse> toResponseList(List<TicketComentario> comentarios);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(TicketComentarioRequest request, @MappingTarget TicketComentario comentario);
}
