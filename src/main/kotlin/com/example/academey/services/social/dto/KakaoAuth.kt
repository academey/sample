package com.example.academey.services.social.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoAuth(
    val accessToken: String,
    val tokenType: String,
    val refreshToken: String,
    val expiresIn: Long = 0,
    val scope: String
)
