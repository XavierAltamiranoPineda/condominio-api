package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.response.ArchivoResponse;
import com.condominio.condominio_api.entity.Archivo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArchivoMapper {

    ArchivoResponse toResponse(Archivo archivo);

    List<ArchivoResponse> toResponseList(List<Archivo> archivos);
}
