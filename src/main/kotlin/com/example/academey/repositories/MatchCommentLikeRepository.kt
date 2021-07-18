package com.example.academey.repositories

import com.example.academey.domain.match.MatchCommentLike
import org.springframework.data.repository.CrudRepository

interface MatchCommentLikeRepository : CrudRepository<MatchCommentLike, Long> {
    fun findByAnonymousUserId(anonymousUserId: Long): MatchCommentLike?
    fun findByUserId(userId: Long): MatchCommentLike?

    fun findByMatchCommentIdAndUserIdAndAnonymousUserId(matchCommentId: Long, userId: Long?, anonymousUserId: Long?): MatchCommentLike?
}
