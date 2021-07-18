package com.example.academey.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration

@Configuration
@ConstructorBinding
@ConfigurationProperties(prefix = "kakao")
class KakaoConfiguration {
    lateinit var clientId: String
    lateinit var url: Url

    data class Url(
        val login: String,
        val token: String,
        val profile: String
    )
}
