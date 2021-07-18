package com.example.academey.services.social.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GoogleProfile(
    val sub: String,
    val name: String,
    val givenName: String,
    val familyName: String,
    val picture: String,
    val locale: String
)
