package com.keerthana.order_management_api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderManagementOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Order Management API")
                .version("1.0")
                .description("REST API for creating, tracking, and managing orders"));
    }
}