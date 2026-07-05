package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.response.HistorialTicketResponse;
import com.condominio.condominio_api.entity.HistorialTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistorialTicketMapper {

    @Mapping(source = "ticket.id", target = "ticketId")
    @Mapping(source = "estado.id", target = "estadoId")
    @Mapping(source = "estado.nombre", target = "estadoNombre")
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.username", target = "usuarioEmail")
    HistorialTicketResponse toResponse(HistorialTicket historialTicket);

    List<HistorialTicketResponse> toResponseList(List<HistorialTicket> historiales);
}
