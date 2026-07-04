package com.condominio.condominio_api.dto.response;

import com.condominio.condominio_api.entity.Usuario.EstadoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String username;
    private EstadoUsuario estado;
    private OffsetDateTime fechaCreacion;
    private OffsetDateTime ultimoLogin;
    private PersonaResponse persona;
    private List<String> roles;
}
