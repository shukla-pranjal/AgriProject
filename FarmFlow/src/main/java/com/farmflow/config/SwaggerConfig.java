package com.farmflow.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Farm Management API")
                        .version("1.0.0")
                        .description("API documentation for the Farm Management System. All responses are wrapped in a GenericResponse object with fields: timestamp (string, date-time), status (string), message (string), and optional data (object).")
                        .contact(new Contact()
                                .name("Pranjal Kumar Shukla & Amritesh Singh")
                                .email("support@farmmanagement.com")
                                .url("https://www.farmmanagement.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization"))
                                .addSchemas("GenericResponse", new Schema<>()
                        .type("object")
                        .properties(Map.of(
                                "timestamp", new Schema<>().type("string").format("date-time").description("Response timestamp"),
                                "status", new Schema<>().type("string").example("success").description("Status of the response"),
                                "message", new Schema<>().type("string").example("Success").description("Response message"),
                                "data", new Schema<>().type("object").description("Response data (varies by endpoint)")
                        )))
                        // Define ErrorResponse schema
                        .addSchemas("ErrorResponse", new Schema<>()
                                .type("object")
                                .properties(Map.of(
                                        "timestamp", new Schema<>().type("string").format("date-time"),
                                        "status", new Schema<>().type("string").example("failed"),
                                        "message", new Schema<>().type("string").example("Error message")
                                )))
                        // Define reusable error responses
                        .addResponses("BadRequest", new ApiResponse()
                                .description("Bad request due to invalid input")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                        .addResponses("Forbidden", new ApiResponse()
                                .description("Access forbidden")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                        .addResponses("NotFound", new ApiResponse()
                                .description("Resource not found")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                        .addResponses("InternalServerError", new ApiResponse()
                                .description("Internal server error")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                );

        List<Server> serverList = List.of(
                new Server().description("Development Server").url("http://localhost:8080"),
                new Server().description("Production Server").url("https://api.farmmanagement.com") // Updated to a more realistic production URL
        );
        openAPI.servers(serverList);

        return openAPI;
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public OperationCustomizer globalResponsesCustomizer() {
        return (operation, handlerMethod) -> {
            // Automatically add global error responses to all operations
            operation.getResponses().putIfAbsent("400", new ApiResponse().$ref("#/components/responses/BadRequest"));
            operation.getResponses().putIfAbsent("403", new ApiResponse().$ref("#/components/responses/Forbidden"));
            operation.getResponses().putIfAbsent("404", new ApiResponse().$ref("#/components/responses/NotFound"));
            operation.getResponses().putIfAbsent("500", new ApiResponse().$ref("#/components/responses/InternalServerError"));
            return operation;
        };
    }
}