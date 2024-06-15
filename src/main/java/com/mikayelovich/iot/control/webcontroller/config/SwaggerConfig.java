package com.mikayelovich.iot.control.webcontroller.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("IOT Master Server")
                        .version("0.1")
                        .description("Iot devices controll API")
                        .contact(new Contact()
                                .name("Arman Arshakyan")
                                .email("ArmanMikayelovich@gmail.com")));
    }
}