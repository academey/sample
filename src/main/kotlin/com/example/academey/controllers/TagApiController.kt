package com.example.academey.controllers

import com.example.academey.controllers.dto.ListResult
import com.example.academey.controllers.dto.SingleResult
import com.example.academey.controllers.dto.TagDto
import com.example.academey.services.ResponseService
import com.example.academey.services.TagService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponses
import io.swagger.annotations.ApiResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping

@Api("/api/tags")
@RestController
@RequestMapping("/api/tags")
class TagApiController(
    private val tagService: TagService,
    private val responseService: ResponseService
) {
    @ApiOperation(value = "태그 전부 조회", notes = "태그를 모조리 조회한다.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 200, message = "success", response = SingleResult::class)
    )
    @GetMapping("")
    fun findAll(): ListResult<TagDto.TagRes> {
        return responseService.getListResult(
            tagService.findAll().map {
                TagDto.TagRes.of(it)
            }
        )
    }
}
