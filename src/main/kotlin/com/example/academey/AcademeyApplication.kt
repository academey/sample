package com.example.academey

import com.example.academey.utils.RestTemplateResponseErrorHandler
import com.example.academey.utils.logger.UpsideLogger
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate

@EnableBatchProcessing
@EnableScheduling
@EnableConfigurationProperties
@SpringBootApplication
class AcademeyApiApplication {
    @Bean
    fun restTemplate(): RestTemplate = RestTemplateBuilder()
        .errorHandler(RestTemplateResponseErrorHandler())
        .requestFactory { HttpComponentsClientHttpRequestFactory() }
        .build()

    companion object : UpsideLogger()
}

fun main(args: Array<String>) {
    runApplication<AcademeyApiApplication>(*args)
}
