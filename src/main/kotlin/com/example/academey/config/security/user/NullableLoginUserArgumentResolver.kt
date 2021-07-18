package com.example.academey.config.security.user

import com.example.academey.config.security.AuthInterceptor.Companion.LOGIN_USER_PK
import com.example.academey.domain.user.User
import com.example.academey.services.UserService
import com.example.academey.utils.CustomExceptions
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class NullableLoginUserArgumentResolver(
    private val userService: UserService
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val isNullableLoginUserAnnotation = parameter.getParameterAnnotation(NullableLoginUser::class.java) != null
        val isUserClass = User::class.java == parameter.parameterType

        return isNullableLoginUserAnnotation && isUserClass
    }

    @Throws(Exception::class)
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): User? {
        val loginUserPk = webRequest.getAttribute(LOGIN_USER_PK, RequestAttributes.SCOPE_REQUEST) as String?
            ?: return null

        return userService.findById(loginUserPk.toLong())
            ?: throw CustomExceptions.TokenNotValidException("토큰에 존재하는 유저가 없습니다")
    }
}
