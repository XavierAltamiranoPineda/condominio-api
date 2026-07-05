package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.TicketRequest;
import com.condominio.condominio_api.dto.response.TicketResponse;
import com.condominio.condominio_api.entity.Ticket;
import com.condominio.condominio_api.entity.Archivo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(source = "persona.id", target = "personaId")
    @Mapping(source = "persona.nombres", target = "personaNombre")
    @Mapping(source = "unidad.id", target = "unidadId")
    @Mapping(source = "unidad.numero", target = "unidadNombre")
    @Mapping(source = "tecnico.id", target = "tecnicoId")
    @Mapping(source = "tecnico.nombres", target = "tecnicoNombre")
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    @Mapping(source = "estadoActual.id", target = "estadoActualId")
    @Mapping(source = "estadoActual.nombre", target = "estadoActualNombre")
    @Mapping(target = "archivosUris", expression = "java(toArchivoUris(ticket.getArchivos()))")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "tecnico", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "estadoActual", ignore = true)
    @Mapping(target = "archivos", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaCierre", ignore = true)
    Ticket toEntity(TicketRequest request);

    List<TicketResponse> toResponseList(List<Ticket> tickets);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "unidad", ignore = true)
    @Mapping(target = "tecnico", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "estadoActual", ignore = true)
    @Mapping(target = "archivos", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaCierre", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(TicketRequest request, @MappingTarget Ticket ticket);

    default List<String> toArchivoUris(List<Archivo> archivos) {
        if (archivos == null) return List.of();
        return archivos.stream()
                .map(a -> "/api/v1/archivos/" + a.getId() + "/download")
                .toList();
    }
}
