package com.example.academey.services

import com.example.academey.domain.match.Match
import com.example.academey.domain.match.MatchComment
import com.example.academey.domain.match.MatchCommentLike
import com.example.academey.domain.match.MatchCommentReport
import com.example.academey.domain.user.AnonymousUser
import com.example.academey.domain.user.User
import com.example.academey.repositories.MatchCommentLikeRepository
import com.example.academey.repositories.MatchCommentReportRepository
import com.example.academey.repositories.MatchCommentRepository
import com.example.academey.utils.CustomExceptions
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.stream.Stream

@Service
class MatchCommentService(
    private val matchCommentRepository: MatchCommentRepository,
    private val matchCommentLikeRepository: MatchCommentLikeRepository,
    private val matchCommentReportRepository: MatchCommentReportRepository
) {
    fun findAllByAnonymousUserId(anonymousUserId: Long): Stream<MatchComment> =
        matchCommentRepository.findByAnonymousUser_Id(anonymousUserId)

    fun findById(id: Long): MatchComment =
        matchCommentRepository.findById(id)
            .orElseThrow { throw CustomExceptions.NotFoundException("해당 id의 매치 커멘트를 찾지 못했습니다") }

    fun findAllByMatchId(matchId: Long, pageable: Pageable): Page<MatchComment> =
        matchCommentRepository.findByMatch_Id(matchId, pageable)

    fun findAll(pageable: Pageable): Page<MatchComment> =
        matchCommentRepository.findAll(pageable)

    fun createComment(
        match: Match,
        user: User?,
        anonymousUser: AnonymousUser?,
        content: String,
        parent: MatchComment?,
        depth: Int
    ): MatchComment {
        if (user == null && anonymousUser == null) throw CustomExceptions.BadRequestException("AnonymousUser 혹은 User 둘 중 하나는 있어야 합니다")

        val matchComment = MatchComment(
            user = user,
            anonymousUser = anonymousUser,
            match = match,
            content = content,
            parent = parent,
            depth = depth
        )
        return matchCommentRepository.save(matchComment)
    }

    fun likeComment(
        matchComment: MatchComment,
        user: User?,
        anonymousUser: AnonymousUser?
    ): MatchCommentLike {
        if (user == null && anonymousUser == null) throw CustomExceptions.BadRequestException("AnonymousUser 혹은 User 둘 중 하나는 있어야 합니다")

        val matchCommentLike = matchCommentLikeRepository.findByMatchCommentIdAndUserIdAndAnonymousUserId(
            matchComment.id,
            user?.id,
            anonymousUser?.id
        )

        return if (matchCommentLike == null) {
            matchCommentLikeRepository.save(
                MatchCommentLike(
                    matchComment = matchComment,
                    user = user,
                    anonymousUser = anonymousUser
                )
            ).also {
                it.matchComment.likeCount += 1
                matchCommentRepository.save(it.matchComment)
            }
        } else {
            matchCommentLike.also {
                it.isLiked = !it.isLiked
                if (it.isLiked) {
                    it.matchComment.likeCount += 1
                } else {
                    it.matchComment.likeCount -= 1
                }
                matchCommentLikeRepository.save(it)
            }
        }
    }

    fun createCommentReport(
        matchComment: MatchComment,
        user: User?,
        anonymousUser: AnonymousUser?,
        reason: String? = null
    ): MatchCommentReport {
        if (user == null && anonymousUser == null) throw CustomExceptions.BadRequestException("AnonymousUser 혹은 User 둘 중 하나는 있어야 합니다")
        val matchCommentReport = MatchCommentReport(
            user = user,
            anonymousUser = anonymousUser,
            matchComment = matchComment,
            reason = reason
        )
        return matchCommentReportRepository.save(matchCommentReport)
    }
}
