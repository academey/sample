package com.example.academey.config.security.user

import com.example.academey.config.security.AuthInterceptor
import com.example.academey.domain.user.AnonymousUser
import com.example.academey.services.AnonymousUserService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class NullableLoginAnonymousUserArgumentResolver(
    private val anonymousUserService: AnonymousUserService
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val isAnonymousUserAnnotation = parameter.getParameterAnnotation(NullableLoginAnonymousUser::class.java) != null
        val isAnonymousUserClass = AnonymousUser::class.java == parameter.parameterType

        return isAnonymousUserAnnotation && isAnonymousUserClass
    }

    @Throws(Exception::class)
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): AnonymousUser? {
        val anonymousUserUid = webRequest.getAttribute(AuthInterceptor.ANONYMOUS_USER_UID, RequestAttributes.SCOPE_REQUEST) as String?
            ?: return null
        return anonymousUserService.signUpIfNotExist(anonymousUserUid)
    }
}
