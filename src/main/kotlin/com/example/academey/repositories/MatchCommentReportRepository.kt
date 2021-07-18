package com.example.academey.repositories

import com.example.academey.domain.match.MatchCommentReport
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.stream.Stream

interface MatchCommentReportRepository : PagingAndSortingRepository<MatchCommentReport, Long> {
    fun findByAnonymousUser_Id(anonymousUserId: Long): Stream<MatchCommentReport>
    fun findByMatchComment_Id(matchCommentId: Long, pageable: Pageable): Page<MatchCommentReport>
}
