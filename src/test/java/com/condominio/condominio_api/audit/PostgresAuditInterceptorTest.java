package com.condominio.condominio_api.audit;

import com.condominio.condominio_api.security.CustomUserDetails;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
public class PostgresAuditInterceptorTest {

    @Autowired
    private PostgresAuditInterceptor auditInterceptor;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("✓ SET LOCAL app.usuario_actual asigna correctamente el ID del usuario en contexto")
    void testSetUsuarioActual() {
        // 1. Configurar contexto de seguridad mockeado con ID específico (ej. 999)
        CustomUserDetails mockUser = new CustomUserDetails(
                999L, "mock_admin", "password", true, true, true, true, Collections.emptyList());
        
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities())
        );

        // 2. Ejecutar el interceptor (que hace el SET LOCAL en la BD)
        auditInterceptor.setUsuarioActual();

        // 3. Verificar en la base de datos (sesión transaccional activa)
        // Pedimos a PostgreSQL que nos devuelva el valor de la variable local que configuró el interceptor
        String userIdInPostgres = (String) entityManager.createNativeQuery(
                "SELECT current_setting('app.usuario_actual', true)"
        ).getSingleResult();

        assertEquals("999", userIdInPostgres, "El ID del usuario no se asignó correctamente en PostgreSQL");
    }
}
