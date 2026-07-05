package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.response.ArchivoResponse;
import com.condominio.condominio_api.entity.Archivo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T22:39:50-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class ArchivoMapperImpl implements ArchivoMapper {

    @Override
    public ArchivoResponse toResponse(Archivo archivo) {
        if ( archivo == null ) {
            return null;
        }

        ArchivoResponse.ArchivoResponseBuilder archivoResponse = ArchivoResponse.builder();

        archivoResponse.id( archivo.getId() );
        archivoResponse.nombre( archivo.getNombre() );
        archivoResponse.ruta( archivo.getRuta() );
        archivoResponse.tipo( archivo.getTipo() );
        archivoResponse.mimeType( archivo.getMimeType() );
        archivoResponse.tamano( archivo.getTamano() );
        archivoResponse.fecha( archivo.getFecha() );

        return archivoResponse.build();
    }

    @Override
    public List<ArchivoResponse> toResponseList(List<Archivo> archivos) {
        if ( archivos == null ) {
            return null;
        }

        List<ArchivoResponse> list = new ArrayList<ArchivoResponse>( archivos.size() );
        for ( Archivo archivo : archivos ) {
            list.add( toResponse( archivo ) );
        }

        return list;
    }
}
