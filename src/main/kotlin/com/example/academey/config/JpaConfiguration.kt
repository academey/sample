package com.example.academey.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.OffsetDateTime
import java.util.Optional

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "utcDateTimeProvider")
class JpaConfiguration {
    @Bean
    fun utcDateTimeProvider(): DateTimeProvider =
        DateTimeProvider { Optional.of(OffsetDateTime.now()) }
}
