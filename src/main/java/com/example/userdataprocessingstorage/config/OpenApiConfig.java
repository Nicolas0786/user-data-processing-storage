package com.example.userdataprocessingstorage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userFileOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User data processing API")
                        .description("Processa arquivos CSV, JSON, XML e consulta usuários")
                        .version("v1")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                );
    }

}
