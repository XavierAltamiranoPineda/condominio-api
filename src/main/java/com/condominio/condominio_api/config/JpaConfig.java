package com.condominio.condominio_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Habilita JPA Auditing para los campos {@code createdAt}, {@code updatedAt},
 * {@code createdBy} y {@code updatedBy} en entidades que extiendan
 * {@link com.condominio.condominio_api.audit.BaseAuditEntity}.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
}
