package com.example.academey.controllers.dto

import com.example.academey.domain.match.MatchComment
import com.example.academey.domain.match.MatchCommentLike
import com.example.academey.domain.match.MatchCommentReport
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Max
import javax.validation.constraints.NotEmpty

class MatchCommentDto {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class CreateMatchCommentReq(
        @field:NotEmpty
        val content: String? = null,
        val parentCommentId: Long? = null,
        @field:Max(1) // 현재로써는 댓글 최대 깊이를 1로 정의합니다.
        val depth: Int = 0
    ) {
        @get:AssertTrue(message = "parent_comment_id가 있으면 depth 가 0 이 어나이어야 하고, parent_comment_id가 없으면 depth 가 0이어어야 합니다")
        private val isValidDepth: Boolean
            get() = (parentCommentId == null && depth == 0) || (parentCommentId != null && depth != 0)
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class CreateCommentReportReq(
        val reason: String?
    )

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class MatchCommentRes(
        val id: Long,
        val user: UserDto.UserRes?,
        val anonymousUser: AnonymousUserDto.AnonymousUserRes?,
        var parent: MatchCommentRes?,
        val depth: Int = 0,
        var likeCount: Long = 0,
        val content: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        companion object {
            fun of(matchComment: MatchComment): MatchCommentRes = MatchCommentRes(
                matchComment.id,
                matchComment.user?.let { UserDto.UserRes.of(it) },
                matchComment.anonymousUser?.let { AnonymousUserDto.AnonymousUserRes.of(it) },
                matchComment.parent?.let { of(it) },
                matchComment.depth,
                matchComment.likeCount,
                matchComment.content,
                matchComment.createdAt,
                matchComment.updatedAt
            )
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class MatchCommentListRes(
        val id: Long,
        val user: UserDto.UserRes?,
        val anonymousUser: AnonymousUserDto.AnonymousUserRes?,
        val children: List<MatchCommentListRes>,
        val depth: Int = 0,
        var likeCount: Long = 0,
        val content: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        companion object {
            fun of(matchComment: MatchComment): MatchCommentListRes =
                MatchCommentListRes(
                    matchComment.id,
                    matchComment.user?.let { UserDto.UserRes.of(it) },
                    matchComment.anonymousUser?.let { AnonymousUserDto.AnonymousUserRes.of(it) },
                    matchComment.children.map {
                        of(it)
                    },
                    matchComment.depth,
                    matchComment.likeCount,
                    matchComment.content,
                    matchComment.createdAt,
                    matchComment.updatedAt
                )
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class MatchCommentReportRes(
        val id: Long,
        val matchComment: MatchCommentRes,
        val reason: String?
    ) {
        companion object {
            fun of(matchCommentReport: MatchCommentReport) = MatchCommentReportRes(
                matchCommentReport.id,
                MatchCommentRes.of(matchCommentReport.matchComment),
                matchCommentReport.reason
            )
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class MatchCommentLikeRes(
        val matchComment: MatchCommentRes,
        val user: UserDto.UserRes?,
        val anonymousUser: AnonymousUserDto.AnonymousUserRes?,
        val isLiked: Boolean
    ) {
        companion object {
            fun of(matchCommentLike: MatchCommentLike) = MatchCommentLikeRes(
                MatchCommentRes.of(matchCommentLike.matchComment),
                matchCommentLike.user?.let { UserDto.UserRes.of(it) },
                matchCommentLike.anonymousUser?.let { AnonymousUserDto.AnonymousUserRes.of(it) },
                matchCommentLike.isLiked
            )
        }
    }
}
