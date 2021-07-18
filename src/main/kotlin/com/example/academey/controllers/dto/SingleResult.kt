package com.example.academey.controllers.dto

class SingleResult<T>(
    override val success: Boolean,
    override val code: Int,
    override val msg: String?,
    val data: T
) : CommonResult(
    success,
    code,
    msg
)
