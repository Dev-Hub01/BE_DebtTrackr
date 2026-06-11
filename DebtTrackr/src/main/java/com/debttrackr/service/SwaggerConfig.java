package com.debttrackr.service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DebtTrackr API")
                        .description("Smart Debt Management System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Suresh")
                                .email("dev.workspace.tech@gmail.com")
                        )
                );
    }
}
