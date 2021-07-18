package com.example.academey.config.security

import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class AnonymousUserProvider {
    fun resolveAnonymousUserUid(request: HttpServletRequest): String? {
        return request.getHeader(ANONYMOUS_USER_UID_HEADER_KEY) ?: null
    }

    companion object {
        const val ANONYMOUS_USER_UID_HEADER_KEY = "X-Anonymous-User-Uid"
    }
}
