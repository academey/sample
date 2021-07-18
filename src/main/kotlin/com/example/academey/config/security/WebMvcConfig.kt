package com.example.academey.config.security

import com.example.academey.config.security.user.LoginAnonymousUserArgumentResolver
import com.example.academey.config.security.user.LoginUserArgumentResolver
import com.example.academey.config.security.user.NullableLoginAnonymousUserArgumentResolver
import com.example.academey.config.security.user.NullableLoginUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val authInterceptor: AuthInterceptor,
    private val loginUserArgumentResolver: LoginUserArgumentResolver,
    private val loginAnonymousUserArgumentResolver: LoginAnonymousUserArgumentResolver,
    private val nullableLoginUserArgumentResolver: NullableLoginUserArgumentResolver,
    private val nullableLoginAnonymousUserArgumentResolver: NullableLoginAnonymousUserArgumentResolver
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            // TODO: 추후 아래로 변경
            // .allowedOrigins(
            //     "http://localhost:3000",
            //     "http://localhost:8081"
            // )
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginUserArgumentResolver)
        resolvers.add(loginAnonymousUserArgumentResolver)
        resolvers.add(nullableLoginUserArgumentResolver)
        resolvers.add(nullableLoginAnonymousUserArgumentResolver)
    }
}
