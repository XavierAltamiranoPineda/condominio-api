package com.condominio.condominio_api.repository;

import com.condominio.condominio_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPersonaId(Long personaId);

    /**
     * Carga el usuario con sus roles y permisos en una sola query.
     * Usado por UserDetailsServiceImpl para autenticación.
     */
    @Query("""
           SELECT u FROM Usuario u
           LEFT JOIN FETCH u.persona p
           LEFT JOIN FETCH u.usuarioRoles ur
           LEFT JOIN FETCH ur.rol r
           WHERE u.username = :username
           """)
    Optional<Usuario> findByUsernameWithRolesAndPermisos(@Param("username") String username);
}
