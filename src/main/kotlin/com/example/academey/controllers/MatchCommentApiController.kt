package com.example.academey.controllers

import com.example.academey.config.security.AnonymousUserProvider.Companion.ANONYMOUS_USER_UID_HEADER_KEY
import com.example.academey.config.security.JwtTokenProvider.Companion.AUTHORIZATION_HEADER_KEY
import com.example.academey.config.security.JwtTokenProvider.Companion.AUTHORIZATION_HEADER_VALUE_FORMAT
import com.example.academey.config.security.user.NullableLoginAnonymousUser
import com.example.academey.config.security.user.NullableLoginUser
import com.example.academey.controllers.dto.MatchCommentDto
import com.example.academey.controllers.dto.SingleResult
import com.example.academey.domain.user.AnonymousUser
import com.example.academey.domain.user.User
import com.example.academey.services.MatchCommentService
import com.example.academey.services.ResponseService
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponses
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiImplicitParams
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import springfox.documentation.annotations.ApiIgnore

@Api("/api/match-comments")
@RestController
@RequestMapping("/api/match-comments")
class MatchCommentApiController(
    private val matchCommentService: MatchCommentService,
    private val responseService: ResponseService
) {
    @ApiOperation(value = "매치 커멘트 신고하기", notes = "매치 댓글을 신고한다.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 201, message = "success")
    )
    @ApiImplicitParams(
        ApiImplicitParam(
            name = AUTHORIZATION_HEADER_KEY,
            value = AUTHORIZATION_HEADER_VALUE_FORMAT,
            dataType = "String",
            paramType = "header"
        ),
        ApiImplicitParam(
            name = ANONYMOUS_USER_UID_HEADER_KEY,
            value = "uid",
            dataType = "String",
            paramType = "header"
        )
    )
    @PostMapping("/{matchCommentId}/report")
    fun reportComment(
        @PathVariable matchCommentId: Long,
        @ApiIgnore @NullableLoginUser user: User?,
        @ApiIgnore @NullableLoginAnonymousUser anonymousUser: AnonymousUser?,
        @RequestBody @Validated requestDto: MatchCommentDto.CreateCommentReportReq
    ): SingleResult<MatchCommentDto.MatchCommentReportRes> =
        matchCommentService.findById(matchCommentId).let { matchComment ->
            matchCommentService.createCommentReport(
                matchComment,
                user,
                anonymousUser,
                requestDto.reason
            ).let { matchCommentReport ->
                responseService.getSingleResult(
                    MatchCommentDto.MatchCommentReportRes.of(matchCommentReport)
                )
            }
        }

    @ApiOperation(value = "매치 커멘트 Like 하기", notes = "매치 댓글을 Like 한다.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 201, message = "success")
    )
    @ApiImplicitParams(
        ApiImplicitParam(
            name = AUTHORIZATION_HEADER_KEY,
            value = AUTHORIZATION_HEADER_VALUE_FORMAT,
            dataType = "String",
            paramType = "header"
        ),
        ApiImplicitParam(
            name = ANONYMOUS_USER_UID_HEADER_KEY,
            value = "uid",
            dataType = "String",
            paramType = "header"
        )
    )
    @PostMapping("/{matchCommentId}/like")
    fun likeComment(
        @PathVariable matchCommentId: Long,
        @ApiIgnore @NullableLoginUser user: User?,
        @ApiIgnore @NullableLoginAnonymousUser anonymousUser: AnonymousUser?
    ): SingleResult<MatchCommentDto.MatchCommentLikeRes> =
        matchCommentService.findById(matchCommentId).let { matchComment ->
            matchCommentService.likeComment(
                matchComment,
                user,
                anonymousUser
            ).let { matchCommentLike ->
                responseService.getSingleResult(
                    MatchCommentDto.MatchCommentLikeRes.of(matchCommentLike)
                )
            }
        }
}
