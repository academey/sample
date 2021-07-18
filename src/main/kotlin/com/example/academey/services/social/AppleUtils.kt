// package com.example.upsideapi.services.social
//
// import com.example.upsideapi.utils.JsonUtils
// import com.fasterxml.jackson.databind.ObjectMapper
// import com.nimbusds.jose.JOSEException
// import com.nimbusds.jose.JWSAlgorithm
// import com.nimbusds.jose.JWSHeader
// import com.nimbusds.jose.JWSSigner
// import com.nimbusds.jose.JWSVerifier
// import com.nimbusds.jose.Payload
// import com.nimbusds.jose.crypto.RSASSAVerifier
// import com.nimbusds.jose.jwk.JWK
// import com.nimbusds.jose.jwk.RSAKey
// import com.nimbusds.jwt.JWTClaimsSet
// import com.nimbusds.jwt.ReadOnlyJWTClaimsSet
// import com.nimbusds.jwt.SignedJWT
// import org.apache.http.client.utils.HttpClientUtils
// import org.springframework.beans.factory.annotation.Value
// import org.springframework.stereotype.Component
// import sun.security.ec.ECPrivateKeyImpl
// import java.io.FileReader
// import java.io.IOException
// import java.security.interfaces.RSAPublicKey
// import java.text.ParseException
// import java.util.Date
// import java.util.HashMap
//
// @Component
// class AppleUtils(
//     private val appleClient: AppleClient
// ) {
//     @Value("\${APPLE.PUBLICKEY.URL}")
//     private val APPLE_PUBLIC_KEYS_URL: String? = null
//
//     @Value("\${APPLE.ISS}")
//     private val ISS: String? = null
//
//     @Value("\${APPLE.AUD}")
//     private val AUD: String? = null
//
//     @Value("\${APPLE.TEAM.ID}")
//     private val TEAM_ID: String? = null
//
//     @Value("\${APPLE.KEY.ID}")
//     private val KEY_ID: String? = null
//
//     @Value("\${APPLE.KEY.PATH}")
//     private val KEY_PATH: String? = null
//
//     @Value("\${APPLE.AUTH.TOKEN.URL}")
//     private val AUTH_TOKEN_URL: String? = null
//
//     @Value("\${APPLE.WEBSITE.URL}")
//     private val APPLE_WEBSITE_URL: String? = null
//
//     /**
//      * User가 Sign in with Apple 요청(https://appleid.apple.com/auth/authorize)으로 전달받은 id_token을 이용한 최초 검증
//      * Apple Document URL ‣ https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/verifying_a_user
//      *
//      * @param id_token
//      * @return boolean
//      */
//     fun verifyIdentityToken(id_token: String?): Boolean {
//         try {
//             val signedJWT = SignedJWT.parse(id_token)
//             val payload: ReadOnlyJWTClaimsSet = signedJWT.jwtClaimsSet
//
//             // EXP
//             val currentTime = Date(System.currentTimeMillis())
//             if (!currentTime.before(payload.getExpirationTime())) {
//                 return false
//             }
//
//             // NONCE(Test value), ISS, AUD
//             if ("20B20D-0S8-1K8" != payload.getClaim("nonce") || ISS != payload.getIssuer() || AUD != payload.getAudience()
//                     .get(0)
//             ) {
//                 return false
//             }
//
//             // RSA
//             if (verifyPublicKey(signedJWT)) {
//                 return true
//             }
//         } catch (e: ParseException) {
//             e.printStackTrace()
//         }
//         return false
//     }
//
//     /**
//      * Apple Server에서 공개 키를 받아서 서명 확인
//      *
//      * @param signedJWT
//      * @return
//      */
//     private fun verifyPublicKey(signedJWT: SignedJWT): Boolean {
//         try {
//             val publicKey = appleClient.getAppleAuthPublicKey()
//             for (key in publicKey.keys) {
//                 val rsaKey: RSAKey = JWK.parse(JsonUtils.objectMapper.writeValueAsString(key)) as RSAKey
//                 val publicKey: RSAPublicKey = rsaKey.toRSAPublicKey()
//                 val verifier: JWSVerifier = RSASSAVerifier(publicKey)
//                 if (signedJWT.verify(verifier)) {
//                     return true
//                 }
//             }
//         } catch (e: Exception) {
//             e.printStackTrace()
//         }
//         return false
//     }
//
//     /**
//      * client_secret 생성
//      * Apple Document URL ‣ https://developer.apple.com/documentation/sign_in_with_apple/generate_and_validate_tokens
//      *
//      * @return client_secret(jwt)
//      */
//     fun createClientSecret(): String {
//         val header = JWSHeader.Builder(JWSAlgorithm.ES256).keyID(KEY_ID).build()
//         val claimsSet = JWTClaimsSet()
//         val now = Date()
//         claimsSet.setIssuer(TEAM_ID)
//         claimsSet.setIssueTime(now)
//         claimsSet.setExpirationTime(Date(now.getTime() + 3600000))
//         claimsSet.setAudience(ISS)
//         claimsSet.setSubject(AUD)
//         val jwt = SignedJWT(header, claimsSet)
//         try {
//             val ecPrivateKey: ECPrivateKey = ECPrivateKeyImpl(readPrivateKey())
//             val jwsSigner: JWSSigner = ECDSASigner(ecPrivateKey.getS())
//             jwt.sign(jwsSigner)
//         } catch (e: InvalidKeyException) {
//             e.printStackTrace()
//         } catch (e: JOSEException) {
//             e.printStackTrace()
//         }
//         return jwt.serialize()
//     }
//
//     /**
//      * 파일에서 private key 획득
//      *
//      * @return Private Key
//      */
//     private fun readPrivateKey(): ByteArray? {
//         val resource: Resource = ClassPathResource(KEY_PATH)
//         var content: ByteArray? = null
//         try {
//             FileReader(resource.getURI().getPath()).use({ keyReader ->
//                 PemReader(keyReader).use({ pemReader ->
//                     run {
//                         val pemObject: PemObject = pemReader.readPemObject()
//                         content = pemObject.getContent()
//                     }
//                 })
//             })
//         } catch (e: IOException) {
//             e.printStackTrace()
//         }
//         return content
//     }
//
//     /**
//      * 유효한 code 인지 Apple Server에 확인 요청
//      * Apple Document URL ‣ https://developer.apple.com/documentation/sign_in_with_apple/generate_and_validate_tokens
//      *
//      * @return
//      */
//     fun validateAuthorizationGrantCode(client_secret: String?, code: String?): TokenResponse? {
//         val tokenRequest: MutableMap<String, String?> = HashMap()
//         tokenRequest["client_id"] = AUD
//         tokenRequest["client_secret"] = client_secret
//         tokenRequest["code"] = code
//         tokenRequest["grant_type"] = "authorization_code"
//         tokenRequest["redirect_uri"] = APPLE_WEBSITE_URL
//         return getTokenResponse(tokenRequest)
//     }
//
//     /**
//      * 유효한 refresh_token 인지 Apple Server에 확인 요청
//      * Apple Document URL ‣ https://developer.apple.com/documentation/sign_in_with_apple/generate_and_validate_tokens
//      *
//      * @param client_secret
//      * @param refresh_token
//      * @return
//      */
//     fun validateAnExistingRefreshToken(client_secret: String?, refresh_token: String?): TokenResponse? {
//         val tokenRequest: MutableMap<String, String?> = HashMap()
//         tokenRequest["client_id"] = AUD
//         tokenRequest["client_secret"] = client_secret
//         tokenRequest["grant_type"] = "refresh_token"
//         tokenRequest["refresh_token"] = refresh_token
//         return getTokenResponse(tokenRequest)
//     }
//
//     /**
//      * POST https://appleid.apple.com/auth/token
//      *
//      * @param tokenRequest
//      * @return
//      */
//     private fun getTokenResponse(tokenRequest: Map<String, String?>?): TokenResponse? {
//         try {
//             val response: String = HttpClientUtils.doPost(AUTH_TOKEN_URL, tokenRequest)
//             val objectMapper = ObjectMapper()
//             val tokenResponse: TokenResponse = objectMapper.readValue(response, TokenResponse::class.java)
//             if (tokenRequest != null) {
//                 return tokenResponse
//             }
//         } catch (e: JsonProcessingException) {
//             e.printStackTrace()
//         }
//         return null
//     } // Test value
//
//     /**
//      * Apple Meta Value
//      *
//      * @return
//      */
//     val metaInfo: Map<String, String?>
//         get() {
//             val metaInfo: MutableMap<String, String?> = HashMap()
//             metaInfo["CLIENT_ID"] = AUD
//             metaInfo["REDIRECT_URI"] = APPLE_WEBSITE_URL
//             metaInfo["NONCE"] = "20B20D-0S8-1K8" // Test value
//             return metaInfo
//         }
//
//     /**
//      * id_token을 decode해서 payload 값 가져오기
//      *
//      * @param id_token
//      * @return
//      */
//     fun decodeFromIdToken(id_token: String?): Payload? {
//         try {
//             val signedJWT = SignedJWT.parse(id_token)
//             val getPayload: ReadOnlyJWTClaimsSet = signedJWT.jwtClaimsSet
//             val objectMapper = ObjectMapper()
//             val payload: Payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), Payload::class.java)
//             if (payload != null) {
//                 return payload
//             }
//         } catch (e: Exception) {
//             e.printStackTrace()
//         }
//         return null
//     }
// }
