package com.xiaoshao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//TODO 这里有一个问题就是文档只会扫描到server模块中的内容，并不会扫描到其他模块下的内容
public class OpenApiConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("智慧问答接口文档")
                        .version("1.0")
                        .description("基于OpenAPI 3规范的接口文档"));
    }

}