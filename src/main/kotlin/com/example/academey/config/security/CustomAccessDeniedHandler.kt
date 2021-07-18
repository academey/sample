package com.example.academey.config.security

import com.example.academey.extensions.writeValue
import com.example.academey.services.ResponseService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomAccessDeniedHandler(
    private val responseService: ResponseService
) : AccessDeniedHandler {
    @Throws(IOException::class, ServletException::class)
    override fun handle(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        // response에 넣기
        httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
        httpServletResponse.status = HttpStatus.FORBIDDEN.value()
        httpServletResponse.outputStream.use { os ->
            os.writeValue(responseService.getFailResult("해당 api에 접근할 수 있는 권한이 아닙니다"))
            os.flush()
        }
    }
}
