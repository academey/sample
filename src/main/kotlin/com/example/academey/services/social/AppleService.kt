package com.example.academey.services.social

import com.example.academey.config.AppleConfiguration
import com.example.academey.services.social.dto.SocialAuth
import com.example.academey.services.social.dto.SocialProfile
import com.example.academey.utils.CustomExceptions
import com.example.academey.utils.JsonUtils
import com.example.academey.utils.fromJson
import com.example.academey.utils.logger.UpsideLogger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Paths
import java.security.PrivateKey
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

// 아래 링크에 따르면, identify token(JWT) 이 넘어오는데,
// 거기서 sub payload 를 사용하면 될 것으로 보인다.
// 이는 클라단과 소통하는 게 더 편할 것으로 보이니 그 때 가서 개발하자.
// https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/authenticating_users_with_sign_in_with_apple#3383773

@Service
class AppleService(
    private val restTemplate: RestTemplate,
    private val appleConfiguration: AppleConfiguration,
    private val appleJwtUtils: AppleJwtUtils
) : BaseSocialService {
    override fun getProfile(idToken: String): SocialProfile {
        val claimsBy = appleJwtUtils.getClaimsBy(idToken, getAppleAuthPublicKey())
        log.error(claimsBy.toString())
        return SocialProfile(
            uid = claimsBy["sub"] as String,
            email = claimsBy["email"] as String,
            picture = null
        )
    }

    override fun getTokenInfo(currentHost: String, code: String): SocialAuth {
        val headers = HttpHeaders().also {
            it.contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code")
        params.add("client_id", appleConfiguration.clientId)
        params.add("client_secret", makeClientSecret())
        params.add("redirect_uri", "$currentHost/social/login/apple")
        params.add("code", code)

        val request = HttpEntity(params, headers)
        val response: ResponseEntity<String> = restTemplate.postForEntity(
            appleConfiguration.url.token,
            request,
            String::class.java
        )
        if (response.statusCode == HttpStatus.OK) {
            return SocialAuth.ofApple(JsonUtils.fromJson(response.body!!))
        } else {
            log.error(params.toString())
            log.error(response.toString())
            log.error(response.body.toString())
            throw CustomExceptions.CommunicationException(response.toString())
        }
    }

    fun getAppleAuthPublicKey(): ApplePublicKeyResponse {
        val response: ResponseEntity<String> = restTemplate.getForEntity(
            appleConfiguration.url.publicKey,
            String::class.java
        )
        return if (response.statusCode == HttpStatus.OK) {
            JsonUtils.fromJson(response.body!!)
        } else {
            log.error(response.toString())
            log.error(response.body.toString())
            throw CustomExceptions.CommunicationException(response.toString())
        }
    }

    override fun getLoginUrl(currentHost: String): StringBuilder =
        StringBuilder()
            .append(appleConfiguration.url.login)
            .append("?client_id=").append(appleConfiguration.clientId)
            .append("&response_type=code")
            .append("&redirect_uri=").append("$currentHost/social/login/apple")

    private fun getPrivateKey(): PrivateKey {
        val resource = ClassPathResource("AuthKey_ZY9X9N2V68.p8")
        val privateKey = String(Files.readAllBytes(Paths.get(resource.uri)))
        val pemReader: Reader = StringReader(privateKey)
        val pemParser = PEMParser(pemReader)
        val converter = JcaPEMKeyConverter()
        val test = pemParser.readObject()
        return converter.getPrivateKey(test as PrivateKeyInfo?)
    }

    @Throws(IOException::class)
    fun makeClientSecret(): String {
        val expirationDate: Date =
            Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant())
        return Jwts.builder()
            .setHeaderParam("kid", KEY_ID)
            .setHeaderParam("alg", "ES256")
            .setIssuer(TEAM_ID)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expirationDate)
            .setAudience("https://appleid.apple.com")
            .setSubject(appleConfiguration.clientId)
            .signWith(SignatureAlgorithm.ES256, getPrivateKey())
            .compact()
    }

    companion object : UpsideLogger() {
        const val KEY_ID = "ZY9X9N2V68"
        const val TEAM_ID = "95KWHG9646"
    }
}
