package com.controle.monitoria_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Sistema de Gerenciamento de Monitoria de Alunos",
                description = "API para gerenciamento de monitoria acadêmica, incluindo IES, escolas, cursos, disciplinas, professores, alunos e relatórios de monitoria",
                version ="v1"
        )
)
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("""
                                                Informe o token JWT obtido no endpoint /login
                                                
                                                Como obter o token:
                                                1. Acesse o endpoint POST/login
                                                2. Informe suas credenciais
                                                3. Copie o token retornado
                                                4. Clique no botão "Authorize" e insira: Bearer {seu-token}
                                                """)));
    }
}
