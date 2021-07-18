package com.example.academey.services.social

import com.example.academey.config.GoogleConfiguration
import com.example.academey.services.social.dto.SocialAuth
import com.example.academey.services.social.dto.SocialProfile
import com.example.academey.utils.CustomExceptions
import com.example.academey.utils.JsonUtils
import com.example.academey.utils.fromJson
import com.example.academey.utils.logger.UpsideLogger
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class GoogleService(
    private val restTemplate: RestTemplate,
    private val googleConfiguration: GoogleConfiguration
) : BaseSocialService {
    companion object : UpsideLogger()

    override fun getProfile(accessToken: String): SocialProfile {
        val headers = HttpHeaders().also {
            it.contentType = MediaType.APPLICATION_FORM_URLENCODED
            it["Authorization"] = "Bearer $accessToken"
        }

        val request = HttpEntity<MultiValueMap<String, String>?>(null, headers)
        val response: ResponseEntity<String> = restTemplate.postForEntity(
            googleConfiguration.url.profile,
            request,
            String::class.java
        )
        return if (response.statusCode == HttpStatus.OK) {
            SocialProfile.ofGoogle(JsonUtils.fromJson(response.body!!))
        } else {
            log.error(response.toString())
            log.error(response.body.toString())
            throw CustomExceptions.CommunicationException(response.toString())
        }
    }

    override fun getTokenInfo(currentHost: String, code: String): SocialAuth {
        val headers = HttpHeaders().also {
            it.contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code")
        params.add("client_id", googleConfiguration.clientId)
        params.add("client_secret", googleConfiguration.clientSecret)
        params.add("redirect_uri", "$currentHost/social/login/google")
        params.add("code", code)
        val request = HttpEntity(params, headers)
        val response: ResponseEntity<String> = restTemplate.postForEntity(
            googleConfiguration.url.token,
            request,
            String::class.java
        )
        return if (response.statusCode == HttpStatus.OK) {
            SocialAuth.ofGoogle(JsonUtils.fromJson(response.body!!))
        } else {
            log.error(response.toString())
            log.error(response.body.toString())
            throw CustomExceptions.CommunicationException(response.toString())
        }
    }

    override fun getLoginUrl(currentHost: String): StringBuilder = StringBuilder()
        .append(googleConfiguration.url.login)
        .append("?client_id=").append(googleConfiguration.clientId)
        .append("&scope=profile")
        .append("&response_type=code")
        .append("&redirect_uri=").append("$currentHost/social/login/google")
}
