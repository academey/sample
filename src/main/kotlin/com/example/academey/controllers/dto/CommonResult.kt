package com.example.academey.controllers.dto

import io.swagger.annotations.ApiModelProperty

open class CommonResult(
    @ApiModelProperty(value = "응답 성공여부 : true/false")
    open val success: Boolean = false,
    @ApiModelProperty(value = "응답 코드 번호 : > 0 정상, < 0 비정상")
    open val code: Int = 0,
    @ApiModelProperty(value = "응답 메시지")
    open val msg: String? = null
)
