package com.example.academey.config.security

import com.example.academey.config.security.user.CustomUserDetailsService
import com.example.academey.domain.user.Role
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.ArrayList
import java.util.Base64
import java.util.Date
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenProvider(
    private val customUserDetailsService: CustomUserDetailsService
) {
    private var secretKey = "webfirewood"

    // 토큰 유효시간 24시간
    private val tokenValidTime = 24 * 60 * 60 * 1000L

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    // JWT 토큰 생성
    fun createToken(userPk: Long, role: Role): String {
        val claims = Jwts.claims().setSubject(userPk.toString()) // JWT payload 에 저장되는 정보단위
        val li = ArrayList<Any>().add(role)
        claims["role"] = li // 정보는 key / value 쌍으로 저장된다.
        val now = Date()
        return Jwts.builder()
            .setClaims(claims) // 정보 저장
            .setIssuedAt(now) // 토큰 발행 시간 정보
            .setExpiration(Date(now.getTime() + tokenValidTime)) // set Expire Time
            .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과
            // signature 에 들어갈 secret값 세팅
            .compact()
    }

    // JWT 토큰에서 인증 정보 조회
    fun getAuthentication(token: String): Authentication {
        val userDetails = customUserDetailsService.loadUserByUsername(getUserPk(token))

        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    // 토큰에서 회원 정보 추출
    fun getUserPk(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "Bearer ${TOKEN}'
    fun resolveToken(request: HttpServletRequest): String? {
        val header = request.getHeader(AUTHORIZATION_HEADER_KEY) ?: return null

        return header.substring("Bearer ".length)
    }

    // 토큰의 유효성 + 만료일자 확인
    fun validateToken(jwtToken: String): Boolean {
        return try {
            val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun addTokenToCookie(response: HttpServletResponse, jwtToken: String) {
        response.addHeader(TOKEN_KEY, jwtToken)
    }

    companion object {
        const val AUTHORIZATION_HEADER_KEY = "Authorization"
        const val AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer"
        const val AUTHORIZATION_HEADER_VALUE_FORMAT = "$AUTHORIZATION_HEADER_VALUE_PREFIX {Token}"
        const val TOKEN_KEY = "X-Bside-Access-Token"
    }
}
