package com.health.healthlakeservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Health Service",
                description = "This is FHIR Health Service",
                contact = @Contact(name = "Sergey Sundukovskiy", email = "ssunduko@gmail.com")),
        security = @SecurityRequirement(name = "capstone"),
        servers = {@Server(url = "http://localhost:8080"), @Server (url = "https://4m0t4oye4j.execute-api.us-east-1.amazonaws.com/prod")}
)
@SecurityScheme(name = "capstone", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@Configuration
/**
 * Open API configuration
 *
 *  This was adapted from a post "OpenAPI Security#Video"
 *  by Cloud-Native AppDev
 *  https://appdev.consulting.redhat.com/tracks/contract-first/security-with-openapi.html#video
 */
public class OpenApiConfig {
}