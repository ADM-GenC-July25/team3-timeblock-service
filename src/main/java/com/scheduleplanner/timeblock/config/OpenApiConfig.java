package com.scheduleplanner.timeblock.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI timeBlockServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("TimeBlock Service API")
                    .description("REST API for managing personal time blocks in the Schedule Planner application")
                    .version("1.0.0")
                    .contact(new Contact()
                        .name("Schedule Planner Team")
                        .email("support@scheduleplanner.com"))
                    .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
} 