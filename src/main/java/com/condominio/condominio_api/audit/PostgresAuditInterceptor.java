package com.condominio.condominio_api.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Inyecta {@code SET LOCAL app.usuario_actual = '<id>'} al inicio de cada
 * transacción de escritura para que los triggers de PostgreSQL (fn_auditoria)
 * puedan registrar el autor del cambio en la tabla {@code auditoria}.
 *
 * <p>Uso: llamar a {@link #setUsuarioActual()} al inicio de cualquier
 * método {@code @Transactional} de escritura que deba quedar auditado.</p>
 *
 * <p>Si no hay usuario autenticado (operaciones de sistema, seeds, Flyway),
 * no se emite el SET LOCAL y los triggers dejan {@code id_usuario = NULL}.</p>
 */
@Slf4j
@Component
public class PostgresAuditInterceptor {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Ejecuta {@code SET LOCAL app.usuario_actual = '<username>'} dentro de la
     * transacción activa. Debe llamarse desde el servicio antes de la operación
     * DML, no desde un filtro HTTP (los filtros no participan en la transacción).
     */
    public void setUsuarioActual() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            log.debug("No hay transacción activa — SET LOCAL omitido");
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return;
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof com.condominio.condominio_api.security.CustomUserDetails userDetails)) {
            return;
        }

        Long userId = userDetails.getId();
        try {
            entityManager.createNativeQuery(
                    "SET LOCAL app.usuario_actual = '" + userId + "'"
            ).executeUpdate();
            log.debug("SET LOCAL app.usuario_actual = '{}'", userId);
        } catch (Exception e) {
            log.warn("No se pudo setear app.usuario_actual: {}", e.getMessage());
        }
    }
}
