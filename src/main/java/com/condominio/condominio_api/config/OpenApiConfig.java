package com.condominio.condominio_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger / SpringDoc OpenAPI 3.
 * Acceso: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI condominioOpenAPI() {
        return new OpenAPI()
                .info(buildInfo())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, buildSecurityScheme()));
    }

    private Info buildInfo() {
        return new Info()
                .title("Condominio API")
                .description("""
                        API REST para el sistema de gestión de condominios.
                        Soporta clientes web, de escritorio y móvil.
                        
                        **Autenticación**: utiliza JWT Bearer Token.
                        Obtén tu token en `POST /api/v1/auth/login` y pégalo en el botón **Authorize** 🔒.
                        """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("Equipo Condominio ESPE")
                        .email("soporte@condominio.local"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private SecurityScheme buildSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Ingresa tu JWT token. Ejemplo: `eyJhbGci...`");
    }
}
