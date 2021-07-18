package com.example.academey.config.security

import com.example.academey.extensions.writeValue
import com.example.academey.services.ResponseService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// https://sas-study.tistory.com/362?category=784778
@Component
class CustomAuthenticationEntryPoint(
    private val responseService: ResponseService
) : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        authException: AuthenticationException
    ) {
        // response에 넣기
        httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
        httpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
        httpServletResponse.outputStream.use { os ->
            os.writeValue(responseService.getFailResult("권한이 없습니다"))
            os.flush()
        }
    }
}
