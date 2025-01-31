package ru.kalimulin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Shop API")
                        .version("1.0")
                        .description("API для интернет-магазина")
                        .contact(new Contact()
                                .name("Denis")
                                .email("deniskalimulin@mail.ru"))
                );
    }
}