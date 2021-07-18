package com.example.academey.controllers

import com.example.academey.controllers.dto.SocialDto
import com.example.academey.domain.user.SocialProvider
import com.example.academey.services.SocialService
import com.example.academey.utils.logger.UpsideLogger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/social/login")
class SocialController(
    private val socialService: SocialService
) {
    /**
     * 소셜 로그인 페이지
     */
    @GetMapping("/view/{provider}")
    fun socialLogin(
        @PathVariable provider: String,
        mav: ModelAndView,
        request: HttpServletRequest
    ): ModelAndView {
        val currentHost = getCurrentHost(request)
        mav.addObject("loginUrl", socialService.getLoginUrl(provider, currentHost))
        mav.viewName = "login"
        return mav
    }

    /**
     * 소셜 인증 완료 후 리다이렉트 화면
     */
    @GetMapping("/{provider}")
    fun socialRedirect(
        mav: ModelAndView,
        @PathVariable provider: String,
        @RequestParam code: String,
        request: HttpServletRequest
    ): ModelAndView {
        val currentHost = getCurrentHost(request)
        mav.addObject("authInfo", socialService.getTokenInfo(provider, currentHost, code))
        mav.viewName = "redirectKakao"
        return mav
    }

    @PostMapping("/sign-in/apple")
    @ResponseStatus(value = HttpStatus.OK)
    fun appleRedirect(
        @RequestBody @Validated requestDto: SocialDto.AppleRedirectReq,
        mav: ModelAndView,
        request: HttpServletRequest
    ): ModelAndView {
        val currentHost = getCurrentHost(request)
        if (requestDto.code != null) {
            log.info(requestDto.toString())
            mav.addObject("authInfo", socialService.getTokenInfo(SocialProvider.APPLE.key, currentHost, requestDto.code))
        } else {
            log.error(requestDto.toString())
        }
        mav.viewName = "redirectKakao"
        return mav
    }

    private fun getCurrentHost(request: HttpServletRequest): String {
        val s = request.requestURL.split("/social")[0]
        if ("localhost" !in s || "0.0.0.0" !in s) {
            return s.replace("http", "https")
        }

        return s
    }

    companion object : UpsideLogger()
}
