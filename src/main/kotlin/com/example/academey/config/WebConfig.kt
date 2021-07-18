package com.example.academey.config

import com.example.academey.config.security.user.LoginUserArgumentResolver
import com.example.academey.config.security.user.NullableLoginUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.data.web.SortHandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val loginUserArgumentResolver: LoginUserArgumentResolver,
    private val nullableLoginUserArgumentResolver: NullableLoginUserArgumentResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(loginUserArgumentResolver)
        argumentResolvers.add(nullableLoginUserArgumentResolver)
        val sortArgumentResolver = SortHandlerMethodArgumentResolver()
        sortArgumentResolver.setSortParameter("sort")
        sortArgumentResolver.propertyDelimiter = "-"

        val pageableArgumentResolver = PageableHandlerMethodArgumentResolver(sortArgumentResolver)
        pageableArgumentResolver.setOneIndexedParameters(true)
        pageableArgumentResolver.setMaxPageSize(500)
        pageableArgumentResolver.setFallbackPageable(PageRequest.of(0, 10))
        argumentResolvers.add(pageableArgumentResolver)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/.well-known/**")
            .addResourceLocations("classpath:/.well-known/")
    }
}
