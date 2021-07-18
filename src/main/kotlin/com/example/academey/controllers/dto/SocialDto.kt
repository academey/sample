package com.example.academey.controllers.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

class SocialDto {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class AppleRedirectReq(
        val code: String?,
        val idToken: String?,
        val user: String?,
        val error: String?,
        val state: String?
    )
}
