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
class LoginUserArgumentResolver(
    private val userService: UserService
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser::class.java) != null
        val isUserClass = User::class.java == parameter.parameterType

        return isLoginUserAnnotation && isUserClass
    }

    @Throws(Exception::class)
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): User {
        // LOGIN_USER_PK 속성이 없다는 소리는 validateToken 을 통과하지 못했다는 의미이므로 NotValidException 이 발생합니다
        val loginUserPk = webRequest.getAttribute(LOGIN_USER_PK, RequestAttributes.SCOPE_REQUEST) as String?
            ?: throw CustomExceptions.TokenNotValidException()

        return userService.findById(loginUserPk.toLong())
            ?: throw CustomExceptions.TokenNotValidException("토큰에 존재하는 유저가 없습니다")
    }
}
