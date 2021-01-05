package com.datav.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 原始访问地址： http://localhost:port/swagger-ui.html
 * 换肤后的地址： http://localhost:port/doc.html
 */
@Component
@EnableSwagger2
public class Swagger2 {


    // 配置swagger2 核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // 指定API类型为Swagger2
                .apiInfo(apiInfo())                     // 定义API汇总信息
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.qyk.controller"))  // 指定controller包所在位置 （也就是为这些controller生成文档）
                .paths(PathSelectors.any())                    // 所有路径下的controller
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("吃货电商平台API")
                .contact(new Contact("祁彦凯", "qiyankai.github.io", "1694577129@qq.com"))
                .description("吃货电商平台API文档")
                .version("1.0.1")
                .termsOfServiceUrl("qiyankai.github.io")
                .build();
    }
}
