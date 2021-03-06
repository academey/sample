package com.example.academey.controllers

import com.example.academey.config.security.AnonymousUserProvider.Companion.ANONYMOUS_USER_UID_HEADER_KEY
import com.example.academey.config.security.user.LoginUser
import com.example.academey.config.security.JwtTokenProvider
import com.example.academey.config.security.JwtTokenProvider.Companion.AUTHORIZATION_HEADER_KEY
import com.example.academey.config.security.JwtTokenProvider.Companion.AUTHORIZATION_HEADER_VALUE_FORMAT
import com.example.academey.config.security.user.NullableLoginAnonymousUser
import com.example.academey.config.security.user.NullableLoginUser
import com.example.academey.controllers.dto.AnonymousUserDto
import com.example.academey.controllers.dto.CommonResult
import com.example.academey.controllers.dto.SingleResult
import com.example.academey.controllers.dto.UserDto
import com.example.academey.domain.user.AnonymousUser
import com.example.academey.domain.user.User
import com.example.academey.services.ResponseService
import com.example.academey.services.SocialService
import com.example.academey.services.UserService
import com.example.academey.utils.CustomExceptions
import com.example.academey.utils.S3Uploader
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.http.HttpServletResponse

@Api("/api/users")
@RestController
@RequestMapping("/api/users")
class UserApiController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val socialService: SocialService,
    private val s3Uploader: S3Uploader,
    private val responseService: ResponseService
) {
    @ApiOperation(value = "????????? ?????? ??????", notes = "???????????? ??????????????????")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 201, message = "success"),
        ApiResponse(code = 400, message = "User Already Exist")
    )
    @PostMapping("/sign-up")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun signUp(
        @RequestBody @Validated requestDto: UserDto.SignUpReq,
        response: HttpServletResponse
    ): SingleResult<UserDto.UserRes> =
        userService.signUp(
            requestDto.email!!,
            requestDto.name!!,
            requestDto.picture,
            requestDto.password!!
        ).let { user ->
            jwtTokenProvider.addTokenToCookie(
                response,
                jwtTokenProvider.createToken(user.id, user.role)
            )
            responseService.getSingleResult(UserDto.UserRes.of(user))
        }

    @ApiImplicitParams(
        ApiImplicitParam(
            name = AUTHORIZATION_HEADER_KEY,
            value = AUTHORIZATION_HEADER_VALUE_FORMAT,
            dataType = "String",
            paramType = "header"
        )
    )
    @ApiOperation(value = "?????? ??????", notes = "?????? ????????? ????????? ????????????.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 401, message = "Unauthorized")
    )
    @DeleteMapping("/sign-out")
    @ResponseStatus(value = HttpStatus.OK)
    fun signOut(
        @ApiIgnore @LoginUser user: User
    ): SingleResult<Boolean> {
        userService.signOut(user)

        return responseService.getSingleResult(
            true
        )
    }

    @ApiOperation(value = "????????? ?????????", notes = "???????????? ???????????????")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 404, message = "Not found")
    )
    @PostMapping("/sign-in")
    @ResponseStatus(value = HttpStatus.OK)
    fun signIn(
        @RequestBody @Validated requestDto: UserDto.SignInReq,
        response: HttpServletResponse
    ): SingleResult<UserDto.UserRes> =
        userService.signIn(
            requestDto.email!!,
            requestDto.password!!
        ).let { user ->
            jwtTokenProvider.addTokenToCookie(
                response,
                jwtTokenProvider.createToken(user.id, user.role)
            )
            return responseService.getSingleResult(
                UserDto.UserRes.of(user)
            )
        }

    @ApiOperation(value = "?????? ????????? ??? ????????????", notes = "?????? ???????????? ??????????????? ????????? ?????? ?????? ????????? ?????? ??????????????? ???")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 400, message = "???????????? ?????? Provider")
    )
    @PostMapping("/sign-in/{provider}", consumes = ["multipart/form-data"])
    @ResponseStatus(value = HttpStatus.OK)
    fun socialIntegratedSignIn(
        @PathVariable provider: String,
        @Validated requestDto: UserDto.SocialReq,
        response: HttpServletResponse
    ) =
        try {
            socialService.socialSignInOrSignUp(
                provider,
                requestDto.access_token,
                requestDto.id_token,
                requestDto.name!!,
                requestDto.user_picture_image?.let {
                    s3Uploader.upload(it, "user-picture")
                },
                requestDto.description,
                requestDto.instagram_name
            ).let { (responseCode, user) ->
                jwtTokenProvider.addTokenToCookie(
                    response,
                    jwtTokenProvider.createToken(user.id, user.role)
                )
                responseService.getSingleResult(
                    UserDto.UserRes.of(user),
                    responseCode.code
                )
            }
        } catch (exception: CustomExceptions.CommunicationException) {
            throw CustomExceptions.BadRequestException("Token??? ???????????? ????????????")
        }

    @ApiImplicitParams(
        ApiImplicitParam(
            name = AUTHORIZATION_HEADER_KEY,
            value = AUTHORIZATION_HEADER_VALUE_FORMAT,
            dataType = "String",
            paramType = "header"
        )
    )
    @ApiOperation(value = "?????? ????????? ????????????", notes = "????????? ???????????? ??????????????????")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 401, message = "Unauthorized")
    )
    @PutMapping("/me", consumes = ["multipart/form-data"])
    @ResponseStatus(value = HttpStatus.OK)
    fun update(
        @Validated requestDto: UserDto.UpdateReq,
        @ApiIgnore @LoginUser user: User
    ): SingleResult<UserDto.UserRes> = userService.update(
        user,
        requestDto.name,
        requestDto.user_picture_image?.let {
            s3Uploader.upload(it, "user-picture")
        }
    ).let {
        responseService.getSingleResult(UserDto.UserRes.of(it))
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
    @ApiOperation(value = "?????? ????????? ??????", notes = "????????? ????????? ????????????")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 401, message = "Unauthorized")
    )
    @GetMapping("/me")
    @ResponseStatus(value = HttpStatus.OK)
    fun get(
        @ApiIgnore @NullableLoginUser user: User?,
        @ApiIgnore @NullableLoginAnonymousUser anonymousUser: AnonymousUser?
    ) =
        when {
            user != null -> {
                responseService.getSingleResult(UserDto.UserRes.of(user))
            }
            anonymousUser != null -> {
                responseService.getSingleResult(AnonymousUserDto.AnonymousUserRes.of(anonymousUser))
            }
            else -> {
                throw CustomExceptions.BadRequestException("$AUTHORIZATION_HEADER_KEY ?????? $ANONYMOUS_USER_UID_HEADER_KEY ??? ??? ????????? ????????? ?????????.")
            }
        }

    @ApiImplicitParams(
        ApiImplicitParam(
            name = AUTHORIZATION_HEADER_KEY,
            value = AUTHORIZATION_HEADER_VALUE_FORMAT,
            dataType = "String",
            paramType = "header"
        )
    )
    @ApiOperation(value = "?????? ?????? ????????????", notes = "?????? ????????? ????????? ??????????????? ???????????? ???????????????.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 401, message = "Unauthorized")
    )
    @GetMapping("/refresh")
    @ResponseStatus(value = HttpStatus.OK)
    fun refreshToken(
        @ApiIgnore @LoginUser user: User,
        response: HttpServletResponse
    ): SingleResult<UserDto.UserRes> {
        jwtTokenProvider.addTokenToCookie(
            response,
            jwtTokenProvider.createToken(user.id, user.role)
        )
        return responseService.getSingleResult(
            UserDto.UserRes.of(user)
        )
    }

    @ApiOperation(value = "?????? ?????? ????????? ??????", notes = "?????? ????????? ???????????? ????????????.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 401, message = "Unauthorized")
    )
    @ApiImplicitParams(
        ApiImplicitParam(
            name = AUTHORIZATION_HEADER_KEY,
            value = AUTHORIZATION_HEADER_VALUE_FORMAT,
            dataType = "String",
            paramType = "header"
        )
    )
    @PatchMapping("/{userId}/nickname")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateUserNickName(
        @PathVariable userId: Long,
        @RequestBody @Validated requestDto: UserDto.UpdateReq,
        @ApiIgnore @LoginUser user: User
    ): SingleResult<UserDto.UserRes> {
        userService.updateUserNickName(
            user, requestDto.name!!
        )

        return responseService.getSingleResult(
            UserDto.UserRes.of(user)
        )
    }

    @ApiOperation(value = "?????? ????????? ?????? ??????", notes = "????????? ???????????? ????????? ????????????.")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = CommonResult::class),
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 401, message = "Unauthorized")
    )
    @GetMapping("/name/duplicate")
    @ResponseStatus(value = HttpStatus.OK)
    fun checkDuplicateName(
        name: String
    ): CommonResult {
        if (userService.isDuplicateName(name)) {
            throw CustomExceptions.UserNickNameDuplicatedException()
        }
        return responseService.getSuccessResult()
    }

    @ApiOperation(value = "?????? ??? ??????", notes = "?????? ??? ????????????")
    @ApiResponses(
        ApiResponse(code = 100, message = "success", response = SingleResult::class),
        ApiResponse(code = 201, message = "success"),
        ApiResponse(code = 400, message = "Pet Already Exist")
    )
    @PostMapping("/pick/{pickedUserId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun pick(
        @ApiIgnore @LoginUser user: User,
        @PathVariable pickedUserId: Long
    ): SingleResult<UserDto.UserFollowRes> =
        userService.follow(
            user,
            userService.findById(pickedUserId) ?: throw CustomExceptions.NotFoundException("pickedUser Not Found")
        ).let { userPick ->
            responseService.getSingleResult(UserDto.UserFollowRes.of(userPick))
        }
}
