package com.example.academey.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration

@Configuration
@ConstructorBinding
@ConfigurationProperties(prefix = "blog")
class ApiProperties {
    lateinit var title: String
    lateinit var banner: Banner

    data class Banner(val title: String? = null, val content: String)
}
