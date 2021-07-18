package com.example.academey.services.social

import com.example.academey.utils.JsonUtils
import com.example.academey.utils.fromJson
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPublicKeySpec
import java.util.Base64

@Component
class AppleJwtUtils {
    fun getClaimsBy(identityToken: String, publicKeyResponse: ApplePublicKeyResponse): Claims {
        try {
            val headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."))
            val header = JsonUtils.fromJson<Map<String, String>>(String(Base64.getDecoder().decode(headerOfIdentityToken)))
            val key = publicKeyResponse.getMatchedKeyBy(header["kid"], header["alg"]) ?: throw NullPointerException("Failed get public key from apple's id server.")

            val nBytes = Base64.getUrlDecoder().decode(key.n)
            val eBytes = Base64.getUrlDecoder().decode(key.e)

            val n = BigInteger(1, nBytes)
            val e = BigInteger(1, eBytes)

            val publicKeySpec = RSAPublicKeySpec(n, e)
            val keyFactory = KeyFactory.getInstance(key.kty)
            val publicKey = keyFactory.generatePublic(publicKeySpec)

            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).body
        } catch (e: NoSuchAlgorithmException) {
        } catch (e: InvalidKeySpecException) {
        } catch (e: SignatureException) {
            // 토큰 서명 검증 or 구조 문제 (Invalid token)
        } catch (e: MalformedJwtException) {
        } catch (e: ExpiredJwtException) {
            // 토큰이 만료됐기 때문에 클라이언트는 토큰을 refresh 해야함.
        } catch (e: Exception) {
            throw e
        }
        throw java.lang.Exception("test")
    }
}
