package br.edu.iff.ccc.webappigormonteiro.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI plataformaOpenAPI(@Value("${app.api.base-url:http://localhost:8080}") String baseUrl) {
        return new OpenAPI()
                .info(new Info()
                        .title("Plataforma de Desafios API")
                        .version("v1")
                        .description("API RESTful para gestão de usuários, categorias e desafios de programação")
                        .contact(new Contact().name("Equipe Plataforma").email("contato@example.com"))
                        .license(new License().name("MIT")))
                .servers(List.of(new Server().url(baseUrl).description("Ambiente configurado")));
    }
}
