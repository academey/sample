package com.example.academey.controllers.dto

import com.example.academey.domain.user.Role
import com.example.academey.domain.user.User
import com.example.academey.domain.user.UserFollow
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.validator.constraints.Length
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

class UserDto {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class SignUpReq(
        @field:Email
        @field:NotEmpty
        val email: String?,
        @field:NotEmpty
        val name: String?,
        val picture: String?,
        val description: String?,
        val instagramName: String?,
        @field:NotEmpty
        @field:Length(min = 6)
        val password: String?
    )

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class SignInReq(
        @field:Email
        @field:NotEmpty
        val email: String?,
        @field:NotEmpty
        @field:Length(min = 6)
        val password: String?
    )

    // form-data 에서는 snakeCase 안 먹음
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class SocialReq(
        val access_token: String?,
        @field:NotEmpty
        val name: String?,
        val id_token: String?,
        var user_picture_image: MultipartFile? = null,
        val description: String?,
        val instagram_name: String?
    )

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class UpdateReq(
        @field:NotEmpty
        val name: String?,
        var user_picture_image: MultipartFile? = null
    )

    data class UserRes(
        val id: Long,
        val email: String?,
        val name: String,
        val picture: String?,
        val followedCount: Long,
        val role: Role
    ) {
        companion object {
            fun of(user: User) = UserRes(
                user.id,
                user.email,
                user.name,
                user.picture,
                user.pickCount,
                user.role
            )
        }
    }

    data class UserFollowRes(
        val user: UserRes,
        val pickedUser: UserRes,
        val isPicked: Boolean
    ) {
        companion object {
            fun of(userFollow: UserFollow) = UserFollowRes(
                userFollow.user.let { UserRes.of(it) },
                userFollow.followedUser.let { UserRes.of(it) },
                userFollow.isPicked
            )
        }
    }
}
