package com.condominio.condominio_api.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Extensión de User de Spring Security para almacenar el ID del usuario
 * autenticado. Esto permite que PostgresAuditInterceptor recupere el ID
 * sin hacer consultas adicionales a la base de datos.
 */
@Getter
public class CustomUserDetails extends User {

    private final Long id;

    public CustomUserDetails(Long id, String username, String password, boolean enabled,
                             boolean accountNonExpired, boolean credentialsNonExpired,
                             boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
}
