package com.github.lilianjaf.mestremenu.core.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI mestreMenuOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mestre Menu API")
                        .description("Ecossistema de gestão compartilhada para o grupo Mestre Menu")
                        .version("v1")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://mestremenu.com.br")));
    }
}
