package com.example.academey.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration

@Configuration
@ConstructorBinding
@ConfigurationProperties(prefix = "aws")
class AwsConfiguration {
    lateinit var s3: S3

    data class S3(
        val bucket: String
    )
}
