package com.example.academey.config.advice

import com.example.academey.controllers.dto.CommonResult
import com.example.academey.services.ResponseService
import com.example.academey.utils.CustomExceptions
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.HashMap
import javax.servlet.http.HttpServletRequest
import org.springframework.validation.BindException

@RestControllerAdvice
class ExceptionAdvice(
    private val responseService: ResponseService
) {
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun defaultException(request: HttpServletRequest, e: Exception): CommonResult {
        e.printStackTrace()
        return responseService.getFailResult(e.message ?: "서버 장애가 발생했습니다")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentNotValidException(request: HttpServletRequest, e: MethodArgumentNotValidException): CommonResult {
        val errors: MutableMap<String, String> = HashMap()
        e.bindingResult.allErrors
            .forEach { c ->
                val field = (c as FieldError).field
                c.getDefaultMessage()?.let { errors.put(field, it) }
            }
        return responseService.getFailResult(errors.toString())
    }

    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun bindException(request: HttpServletRequest, e: BindException): CommonResult {
        val errors: MutableMap<String, String> = HashMap()
        e.bindingResult.allErrors
            .forEach { c ->
                val field = (c as FieldError).field
                c.getDefaultMessage()?.let { errors.put(field, it) }
            }
        return responseService.getFailResult(errors.toString())
    }

    @ExceptionHandler(CustomExceptions.UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected fun userNotFound(request: HttpServletRequest, e: CustomExceptions.UserNotFoundException): CommonResult {
        return responseService.getFailResult(e.message ?: "해당 유저가 발견되지 않았습니다.")
    }

    @ExceptionHandler(CustomExceptions.EmailSignInFailedException::class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected fun emailSignInFailed(
        request: HttpServletRequest,
        e: CustomExceptions.EmailSignInFailedException
    ): CommonResult {

        return responseService.getFailResult(e.message ?: "계정이 존재하지 않거나 이메일 또는 비밀번호가 정확하지 않습니다.")
    }
    @ExceptionHandler(CustomExceptions.UserNickNameDuplicatedException::class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected fun duplicateUserNickName(
        request: HttpServletRequest,
        e: CustomExceptions.UserNickNameDuplicatedException
    ): CommonResult {
        return responseService.getFailResult(e.message ?: "이미 존재하고 있는 닉네임 입니다.")
    }

    @ExceptionHandler(CustomExceptions.AuthenticationEntryPointException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun authenticationEntryPointException(
        request: HttpServletRequest,
        e: CustomExceptions.AuthenticationEntryPointException
    ): CommonResult {
        return responseService.getFailResult(e.message ?: "해당 리소스에 접근하기 위한 권한이 없습니다.")
    }

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun accessDeniedException(request: HttpServletRequest, e: AccessDeniedException): CommonResult {
        return responseService.getFailResult(e.message ?: "보유한 권한으로 접근할수 없는 리소스 입니다.")
    }

    @ExceptionHandler(CustomExceptions.CommunicationException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun communicationException(request: HttpServletRequest, e: CustomExceptions.CommunicationException): CommonResult {
        return responseService.getFailResult(e.message ?: "통신 중 오류가 발생하였습니다.")
    }

    @ExceptionHandler(CustomExceptions.NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundException(request: HttpServletRequest, e: CustomExceptions.NotFoundException): CommonResult {
        return responseService.getFailResult(e.message ?: "해당 리소스를 발견하지 못했습니다")
    }

    @ExceptionHandler(CustomExceptions.BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun badRequestException(request: HttpServletRequest, e: CustomExceptions.BadRequestException): CommonResult {
        return responseService.getFailResult(e.message ?: "잘못된 요청입니다")
    }

    @ExceptionHandler(CustomExceptions.UserExistException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun communicationException(request: HttpServletRequest, e: CustomExceptions.UserExistException): CommonResult {
        return responseService.getFailResult(e.message ?: "이미 가입한 회원입니다. 로그인을 해주십시오.")
    }

    @ExceptionHandler(CustomExceptions.ProviderNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun communicationException(request: HttpServletRequest, e: CustomExceptions.ProviderNotFoundException): CommonResult {
        return responseService.getFailResult(e.message ?: "해당 Provider 는 지원하지 않습니다")
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected fun userNameNotFound(request: HttpServletRequest, e: UsernameNotFoundException): CommonResult {
        return responseService.getFailResult(e.message ?: "해당 유저가 발견되지 않았습니다.")
    }

    @ExceptionHandler(CustomExceptions.TokenNotValidException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun communicationException(request: HttpServletRequest, e: CustomExceptions.TokenNotValidException): CommonResult {
        return responseService.getFailResult(e.message ?: "해당 토큰은 유효하지 않습니다.")
    }

    @ExceptionHandler(CustomExceptions.TokenNotExistException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun communicationException(request: HttpServletRequest, e: CustomExceptions.TokenNotExistException): CommonResult {
        return responseService.getFailResult(e.message ?: "토큰이 존재하지 않습니다")
    }

    @ExceptionHandler(CustomExceptions.CreateMatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun communicationException(request: HttpServletRequest, e: CustomExceptions.CreateMatchException): CommonResult {
        return responseService.getFailResult(1009, e.message ?: "매치생성에 실패하였습니다.")
    }

    @ExceptionHandler(CustomExceptions.MatchNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun communicationException(request: HttpServletRequest, e: CustomExceptions.MatchNotFoundException): CommonResult {
        return responseService.getFailResult(1008, e.message ?: "존재하지 않은 매치입니다.")
    }
}
