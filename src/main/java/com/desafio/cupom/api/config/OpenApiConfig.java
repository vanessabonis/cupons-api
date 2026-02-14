package com.desafio.cupom.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cupomOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Cupons de Desconto")
                .description("API REST para gerenciamento de cupons de desconto")
                .version("0.1.0")
                .contact(new Contact()
                    .name("Vanessa Bonis")
                    .email("vanessa1302@gmail.com")
                    .url("https://github.com/vanessabonis"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}

