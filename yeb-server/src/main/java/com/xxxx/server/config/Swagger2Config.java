package com.xxxx.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置类
 * @author: gouzi
 * @create: 2021-07-31 13:28
 **/
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket creatRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xxxx.server.controller"))
                .build()
                // 添加授权功能
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }


    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("云e办接口文档")
                .description("在线办公系统后台api管理")
                .contact(new Contact("gouzi", "www.gouzi.com", "abc@163.com"))
                .version("1.0")
                .build();
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/hello/.*"));
        return result;
    }

    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }

    private List<? extends SecurityScheme> securitySchemes() {
        List<ApiKey> result = new ArrayList<>();
        result.add(new ApiKey("Authorization", "Authorization", "header"));
        return result;
    }
}
