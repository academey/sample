package com.example.academey.controllers.dto

class ListResult<T>(
    override val success: Boolean,
    override val code: Int,
    override val msg: String?,
    val data: List<T>
) : CommonResult(
    success,
    code,
    msg
)
