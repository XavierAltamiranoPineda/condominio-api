package com.condominio.condominio_api.mapper;

import com.condominio.condominio_api.dto.request.UsuarioRequest;
import com.condominio.condominio_api.dto.response.UsuarioResponse;
import com.condominio.condominio_api.entity.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-04T21:07:43-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Autowired
    private PersonaMapper personaMapper;

    @Override
    public UsuarioResponse toResponse(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioResponse.UsuarioResponseBuilder usuarioResponse = UsuarioResponse.builder();

        usuarioResponse.id( usuario.getId() );
        usuarioResponse.username( usuario.getUsername() );
        usuarioResponse.estado( usuario.getEstado() );
        usuarioResponse.fechaCreacion( usuario.getFechaCreacion() );
        usuarioResponse.ultimoLogin( usuario.getUltimoLogin() );
        usuarioResponse.persona( personaMapper.toResponse( usuario.getPersona() ) );

        usuarioResponse.roles( toRoleNameList(usuario.getUsuarioRoles()) );

        return usuarioResponse.build();
    }

    @Override
    public Usuario toEntity(UsuarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setUsername( request.getUsername() );
        usuario.setEstado( request.getEstado() );

        return usuario;
    }

    @Override
    public void updateFromRequest(UsuarioRequest request, Usuario usuario) {
        if ( request == null ) {
            return;
        }

        if ( request.getUsername() != null ) {
            usuario.setUsername( request.getUsername() );
        }
        if ( request.getEstado() != null ) {
            usuario.setEstado( request.getEstado() );
        }
    }
}
