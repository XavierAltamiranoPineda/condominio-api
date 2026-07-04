package com.condominio.condominio_api.security;

import com.condominio.condominio_api.entity.Usuario;
import com.condominio.condominio_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * UserDetailsService real — carga el usuario desde PostgreSQL.
 *
 * <p>Una sola query (JOIN FETCH) carga: Usuario → Persona → Roles → Permisos.
 * Las authorities incluyen tanto roles (ROLE_ADMIN) como permisos (CUOTAS_LEER),
 * lo que permite usar {@code @PreAuthorize("hasAuthority('CUOTAS_LEER')")}
 * en controllers para control granular.</p>
 *
 * <p>Usuario BLOQUEADO o INACTIVO → {@code UsernameNotFoundException}
 * para no revelar el motivo exacto del fallo al cliente.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository
                .findByUsernameWithRolesAndPermisos(username)
                .orElseThrow(() -> {
                    log.warn("Intento de login con usuario inexistente: {}", username);
                    return new UsernameNotFoundException("Credenciales inválidas");
                });

        if (usuario.getEstado() != Usuario.EstadoUsuario.ACTIVO) {
            log.warn("Intento de login con usuario no activo: {} estado={}", username, usuario.getEstado());
            throw new UsernameNotFoundException("Credenciales inválidas");
        }

        List<GrantedAuthority> authorities = buildAuthorities(usuario);

        return new CustomUserDetails(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPasswordHash(),
                usuario.getEstado() != Usuario.EstadoUsuario.INACTIVO,
                true,
                true,
                usuario.getEstado() != Usuario.EstadoUsuario.BLOQUEADO,
                authorities
        );
    }

    /**
     * Construye la lista de authorities combinando roles (ROLE_X) y permisos (MODULO_ACCION).
     * Esto permite control tanto por rol como por permiso granular en @PreAuthorize.
     */
    private List<GrantedAuthority> buildAuthorities(Usuario usuario) {
        Stream<GrantedAuthority> roles = usuario.getUsuarioRoles().stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRol().getNombre()));

        Stream<GrantedAuthority> permisos = usuario.getUsuarioRoles().stream()
                .flatMap(ur -> ur.getRol().getRolPermisos().stream())
                .map(rp -> new SimpleGrantedAuthority(rp.getPermiso().getModulo() + "_" + rp.getPermiso().getAccion()));

        return Stream.concat(roles, permisos).distinct().toList();
    }
}
