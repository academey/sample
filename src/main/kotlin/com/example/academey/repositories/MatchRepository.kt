package com.example.academey.repositories

import com.example.academey.domain.match.Match
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface MatchRepository : PagingAndSortingRepository<Match, Long> {
    fun findByTitle(email: String): Match?
    fun findByIdIn(ids: Collection<Long>): Iterable<Match>
    fun findAllByOrderByCreatedAtDesc(): Iterable<Match>
    fun findAllByOrderByTitleDesc(): Iterable<Match>
    override fun findAll(pageable: Pageable): Page<Match>
}
