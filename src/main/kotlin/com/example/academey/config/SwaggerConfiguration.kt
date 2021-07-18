package com.example.academey.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
            .apis(RequestHandlerSelectors.basePackage("com.example.upsideapi.controllers"))
            .paths(PathSelectors.ant("/api/**"))
            .build()
            .useDefaultResponseMessages(false) // 기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않음
    }

    private fun swaggerInfo(): ApiInfo {
        return ApiInfoBuilder().title("Upside API Documentation")
            .description("앱 개발시 사용되는 서버 API에 대한 연동 문서입니다")
            .license("upside").licenseUrl("https://github.com/bsideproject/upside-api").version("1").build()
    }
}
