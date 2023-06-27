package wv.webscraping.com.brasileiraoapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String BASE_PACKAGE = "wv.webscraping.com.brasileiraoapi.controller";
    private static final String API_TITULO = "Brasileirao API - Scraping";
    private static final String API_DESCRICAO = "Rest API that obtains data from Brasileirão matches in real time";
    private static final String API_VERSAO = "1.0.0";
    private static final String CONTATO_NOME = "Weslley Vander Olieria da Silva";
    private static final String CONTATO_GITHUB = "https://github.com/weslleyg2/weslleyg2";
    private static final String CONTATO_EMAIL = "weslleyvander2009@gmail.com";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(buildApiInfo());
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title(API_TITULO)
                .description(API_DESCRICAO)
                .version(API_VERSAO)
                .contact(new Contact(CONTATO_NOME, CONTATO_GITHUB, CONTATO_EMAIL))
                .build();
    }
}
