package com.example.academey.services.social

import com.example.academey.config.KakaoConfiguration
import com.example.academey.services.social.dto.SocialAuth
import com.example.academey.services.social.dto.SocialProfile
import com.example.academey.utils.CustomExceptions
import com.example.academey.utils.JsonUtils
import com.example.academey.utils.fromJson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

// https://daddyprogrammer.org/post/1012/springboot2-rest-api-social-login-kakao/
@Service
class KakaoService(
    private val restTemplate: RestTemplate,
    private val kakaoConfiguration: KakaoConfiguration
) : BaseSocialService {
    override fun getProfile(accessToken: String): SocialProfile {
        val headers = HttpHeaders().also {
            it.contentType = MediaType.APPLICATION_FORM_URLENCODED
            it["Authorization"] = "Bearer $accessToken"
        }

        val request = HttpEntity<MultiValueMap<String, String>?>(null, headers)
        val response: ResponseEntity<String> = restTemplate.postForEntity(
            kakaoConfiguration.url.profile,
            request,
            String::class.java
        )

        return if (response.statusCode == HttpStatus.OK) {
            SocialProfile.ofKakao(JsonUtils.fromJson(response.body!!))
        } else {
            GoogleService.log.error(response.toString())
            GoogleService.log.error(response.body.toString())
            throw CustomExceptions.CommunicationException(response.toString())
        }
    }

    override fun getTokenInfo(currentHost: String, code: String): SocialAuth {
        val headers = HttpHeaders().also {
            it.contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code")
        params.add("client_id", kakaoConfiguration.clientId)
        params.add("redirect_uri", "$currentHost/social/login/kakao")
        params.add("code", code)

        val request = HttpEntity(params, headers)
        val response: ResponseEntity<String> = restTemplate.postForEntity(
            kakaoConfiguration.url.token,
            request,
            String::class.java
        )
        return if (response.statusCode == HttpStatus.OK) {
            SocialAuth.ofKakao(JsonUtils.fromJson(response.body!!))
        } else {
            GoogleService.log.error(response.toString())
            GoogleService.log.error(response.body.toString())
            throw CustomExceptions.CommunicationException(response.toString())
        }
    }

    override fun getLoginUrl(currentHost: String): StringBuilder {
        return StringBuilder()
            .append(kakaoConfiguration.url.login)
            .append("?client_id=").append(kakaoConfiguration.clientId)
            .append("&response_type=code")
            .append("&redirect_uri=").append("$currentHost/social/login/kakao")
    }
}
