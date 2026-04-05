package edu.eci.dosw.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para documentación de API y seguridad JWT.
 * Define el esquema de seguridad Bearer JWT que aparecerá en Swagger UI.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "DOSW-Library API",
                version = "1.0.0",
                description = "API REST para gestión de biblioteca con Spring Boot 3.2.3, Java 21 y MongoDB",
                contact = @Contact(name = "DOSW Team", email = "info@dosw.com")
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Introduzca el token JWT obtenido de POST /auth/login"
)
public class OpenAPIConfig {
}
