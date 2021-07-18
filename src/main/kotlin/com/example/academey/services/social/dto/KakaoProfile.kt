package com.example.academey.services.social.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoProfile(
    val id: Long,
    val properties: Properties
) {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class Properties(
        val nickname: String,
        val thumbnailImage: String?,
        val profileImage: String?
    )
}
