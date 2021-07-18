package com.example.academey.services

import com.example.academey.controllers.dto.CommonResult

import com.example.academey.controllers.dto.ListResult

import com.example.academey.controllers.dto.SingleResult
import org.springframework.stereotype.Service

@Service
class ResponseService {
    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    enum class ResponseCode(var code: Int, var msg: String) {
        SUCCESS(0, "성공하였습니다."),
        FAIL(-1, "실패했습니다"),
        SOCIAL_SIGN_IN(100, "로그인 되었습니다"),
        SOCIAL_SIGN_UP(101, "회원가입 되었습니다"),
        SHOULD_HAVE_EXPERIENCED_MATCH(200, "매치에 참가해야만 랭킹을 확인할 수 있습니다")
    }

    // 단일건 결과를 처리하는 메소드
    fun <T> getSingleResult(data: T, code: Int = ResponseCode.SUCCESS.code, additionalData: Map<String, Any>? = null): SingleResult<T> = SingleResult(
        true,
        code,
        ResponseCode.SUCCESS.msg,
        data
    )

    // 다중건 결과를 처리하는 메소드
    fun <T> getListResult(list: List<T>, code: Int = ResponseCode.SUCCESS.code, additionalData: Map<String, Any>? = null): ListResult<T> = ListResult(
        true,
        code,
        ResponseCode.SUCCESS.msg,
        list
    )

    // 실패 결과만 처리하는 메소드
    fun getFailResult(msg: String?): CommonResult = CommonResult(
        false,
        ResponseCode.FAIL.code,
        msg
    )
    // 실패 결과만 처리하는 메소드(코드 , 메시지)
    fun getFailResult(code: Int, msg: String?): CommonResult = CommonResult(
        false,
        code,
        msg
    )

    fun getSuccessResult(): CommonResult = CommonResult(
        true,
        ResponseCode.SUCCESS.code,
        ResponseCode.SUCCESS.msg
    )
}
