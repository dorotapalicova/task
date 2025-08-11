package com.task.user_manager.swagger.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApiConfiguration() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("User Management API")
                                .version("1.0")
                                .description("User Management API documentation")
                                .contact(
                                        new Contact()
                                                .email("dorotapalicova@gmail.com")
                                                .name("Dorota Palicova")
                                )
                );
    }
}