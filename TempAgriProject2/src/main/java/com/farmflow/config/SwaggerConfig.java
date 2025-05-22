package com.farmflow.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI =  new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Farm Management API")
                        .version("1.0.0")
                        .description("API documentation for the Farm Management System, providing endpoints for managing carts, categories, farmers, orders, payments, products, reviews, and users.")
                        .contact(new Contact()
                                .name("Pranjal Kumar Shukla & Amritesh Singh")
                                .email("emgail&com")
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
                    );

        List<Server> serverList = List.of(
                new Server().description("Development Server").url("http://localhost:8080"),
//                new Server().description("Testing Server").url("http://localhost:8081"),
                new Server().description("Production Server").url("https://localhost:8080")
        );
        openAPI.servers(serverList);
        if (openAPI.getComponents() == null) {
            openAPI.setComponents(new Components());
        }


        return openAPI;
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}