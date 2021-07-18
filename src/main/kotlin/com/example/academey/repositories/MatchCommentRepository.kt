package com.example.academey.repositories

import com.example.academey.domain.match.MatchComment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.stream.Stream

interface MatchCommentRepository : PagingAndSortingRepository<MatchComment, Long> {
    fun findByAnonymousUser_Id(anonymousUserId: Long): Stream<MatchComment>
    fun findByMatch_Id(matchId: Long, pageable: Pageable): Page<MatchComment>

    override fun findAll(pageable: Pageable): Page<MatchComment>
}
