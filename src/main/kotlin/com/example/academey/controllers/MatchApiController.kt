package com.example.academey.controllers

import com.example.academey.config.security.AnonymousUserProvider.Companion.ANONYMOUS_USER_UID_HEADER_KEY
import com.example.academey.config.security.JwtTokenProvider.Companion.AUTHORIZATION_HEADER_KEY
import com.example.academey.config.security.JwtTokenProvider.Companion.AUTHORIZATION_HEADER_VALUE_FORMAT
import com.example.academey.config.security.user.NullableLoginAnonymousUser
import com.example.academey.config.security.user.NullableLoginUser
import com.example.academey.controllers.dto.ListResult
import com.example.academey.controllers.dto.MatchDto
import com.example.academey.controllers.dto.MatchCommentDto
import com.example.academey.controllers.dto.SingleResult
import com.example.academey.domain.user.AnonymousUser
import com.example.academey.services.MatchService
import com.example.academey.domain.user.User
import com.example.academey.services.MatchCommentService
import com.example.academey.services.ResponseService
import com.example.academey.utils.CustomExceptions
import com.example.academey.utils.S3Uploader
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import springfox.documentation.annotations.ApiIgnore
import java.sql.SQLException
import javax.validation.Valid

// TODO : 일반적인 애로 사항에 대한 코드 정의 필요. ( 우선은 데이터 없을 경우, 일반적인 CRUD에러에 대해 도메인 별로 만들지..등? 협의 필요)
// TODO : 응답에 대한 실패 처리를  HTTP 응답 코드로 할지 아니면 응답값 안에 별도의 코드로 처리를 할지 협의 필요.
// TODO : 수정 , 삭제 개발
@Api("/api/matches")
@RestController
@RequestMapping("/api/matches")
class MatchApiController(
    private val matchService: MatchService,
    private val matchCommentService: MatchCommentService,
    private val responseService: ResponseService,
    private val s3Uploader: S3Uploader
) {
    @ApiOperation(
        value = "매치 생성",
        notes = "매치를 생성한다. 이 때, start_at, end_at은 nullable 이며 2011-11-11T11:11:11 와 같은 포맷으로 보내주면 된다."
    )
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 201, message = "success")
    )
    @PostMapping("")
    fun create(
        @Valid requestDto: MatchDto.CreateMatchReq
    ): SingleResult<MatchDto.MatchWithStatusRes> {
        try {
            val match = matchService.create(
                requestDto.title!!,
                requestDto.thumbnail_image?.let {
                    s3Uploader.upload(it, "match-thumbnail")
                } ?: throw CustomExceptions.BadRequestException("thumbnail_image 를 보내주세요"),
                requestDto.icon!!,
                requestDto.startAt,
                requestDto.endAt,
                requestDto.tags!!
            )

            return responseService.getSingleResult(
                MatchDto.MatchWithStatusRes.of(match)
            )
        } catch (e: SQLException) {
            throw CustomExceptions.CreateMatchException("매치생성실패")
        } catch (e: CustomExceptions.CreateMatchException) {
            throw e
        }
    }

    @ApiOperation(value = "매치 리스트 조회", notes = "매치를 오더 타입으로 조회한다.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = ListResult::class),
        ApiResponse(code = 200, message = "success")
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
        ),
        ApiImplicitParam(
            name = "size",
            value = "사이즈",
            required = true,
            dataType = "int",
            paramType = "query",
            defaultValue = ""
        ),
        ApiImplicitParam(
            name = "page",
            value = "페이지",
            required = true,
            dataType = "int",
            paramType = "query",
            defaultValue = ""
        ),
        ApiImplicitParam(
            name = "sort",
            value = "정렬기준",
            required = true,
            dataType = "string",
            paramType = "query",
            defaultValue = "",
            allowableValues = "title-desc, title-asc , createdAt-desc , createdAt-asc"
        )
    )
    @GetMapping("")
    fun findAll(
        @ApiIgnore @NullableLoginUser user: User?,
        @ApiIgnore @NullableLoginAnonymousUser anonymousUser: AnonymousUser?,
        pageable: Pageable
    ): ListResult<MatchDto.MatchWithStatusRes> {
        if (user == null && anonymousUser == null) throw CustomExceptions.BadRequestException("AnonymousUser 혹은 User 둘 중 하나는 있어야 합니다")
        val list = matchService.findAllMatch(pageable).toList()

        return responseService.getListResult(
            list.map {
                MatchDto.MatchWithStatusRes.of(
                    it
                )
            }
        )
    }

    @ApiOperation(value = "매치 단일 조회", notes = "매치 하나를 조회한다.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 200, message = "success")
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
    @GetMapping("/{matchId}")
    fun findOne(
        @PathVariable matchId: Long,
        @ApiIgnore @NullableLoginUser user: User?,
        @ApiIgnore @NullableLoginAnonymousUser anonymousUser: AnonymousUser?
    ): SingleResult<MatchDto.MatchWithStatusRes> {
        val match = matchService.findById(matchId)
        return responseService.getSingleResult(
            MatchDto.MatchWithStatusRes.of(
                match
            )
        )
    }

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
    @ApiOperation(value = "매치 커멘트 생성", notes = "매치에 댓글을 단다.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 201, message = "success")
    )
    @PostMapping("/{matchId}/comments")
    fun createComment(
        @PathVariable matchId: Long,
        @ApiIgnore @NullableLoginUser user: User?,
        @ApiIgnore @NullableLoginAnonymousUser anonymousUser: AnonymousUser?,
        @RequestBody @Validated requestDto: MatchCommentDto.CreateMatchCommentReq
    ): SingleResult<MatchCommentDto.MatchCommentRes> =
        matchService.findById(matchId).let { match ->
            matchCommentService.createComment(
                match,
                user,
                anonymousUser,
                requestDto.content!!,
                requestDto.parentCommentId?.let(matchCommentService::findById),
                requestDto.depth
            ).let { matchComment ->
                responseService.getSingleResult(
                    MatchCommentDto.MatchCommentRes.of(matchComment)
                )
            }
        }

    @ApiOperation(value = "매치 커멘트 리스트 조회", notes = "매치 댓글들을 조회한 조회한다.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = ListResult::class),
        ApiResponse(code = 200, message = "success")
    )
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "size",
            value = "사이즈",
            required = true,
            dataType = "int",
            paramType = "query",
            defaultValue = ""
        ),
        ApiImplicitParam(
            name = "page",
            value = "페이지",
            required = true,
            dataType = "int",
            paramType = "query",
            defaultValue = ""
        )
    )
    @GetMapping("/{matchId}/comments")
    fun findComments(
        @PathVariable matchId: Long,
        pageable: Pageable
    ): ListResult<MatchCommentDto.MatchCommentListRes> =
        matchService.findById(matchId).let {
            responseService.getListResult(
                matchCommentService.findAllByMatchId(matchId, pageable).toList().map {
                    MatchCommentDto.MatchCommentListRes.of(it)
                }
            )
        }
}
