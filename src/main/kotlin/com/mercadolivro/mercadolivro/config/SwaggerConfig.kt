package com.mercadolivro.mercadolivro.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

//só acessa o swagger quando tiver em perfil diferente de prod (produção)
@Profile("!prod")
@Configuration
 //ativando o swagger2
class SwaggerConfig {


    @Bean
    fun api(): Docket{
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.mercadolivro.mercadolivro.controller"))
            .paths(PathSelectors.any())
            .build()

            .apiInfo(ApiInfoBuilder()
                .title("Mercado Livro")
                .description("Api do Mercado livro")
                .build())
    }
}