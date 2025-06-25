package com.app.park_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPi() {
        return new OpenAPI()
                .info(
                    new Info()
                    .title("REST API - Spring Park")
                    .description("API for managing parking lots and users")
                    .version("1.0.0")
                    .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"))
                    .contact(new Contact().name("Erick Ribeiro").email("erick@mail.com"))
                );
    }
    
}
