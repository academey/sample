package com.example.academey.config.security

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
    private val anonymousUserProvider: AnonymousUserProvider
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val token = jwtTokenProvider.resolveToken(request)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val userPk = jwtTokenProvider.getUserPk(token)
            request.setAttribute(LOGIN_USER_PK, userPk)
        }
        val anonymousUserUid = anonymousUserProvider.resolveAnonymousUserUid(request)
        if (anonymousUserUid != null) {
            request.setAttribute(ANONYMOUS_USER_UID, anonymousUserUid)
        }

        return true
    }

    companion object {
        const val LOGIN_USER_PK = "loginUserPk"
        const val ANONYMOUS_USER_UID = "anonymousUserUid"
    }
}
