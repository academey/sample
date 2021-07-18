package com.example.academey.services

import com.example.academey.domain.match.Match
import com.example.academey.domain.match.MatchAndTag
import com.example.academey.repositories.MatchRepository
import com.example.academey.repositories.TagRepository
import com.example.academey.utils.CustomExceptions
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MatchService(
    private val matchRepository: MatchRepository,
    private val tagRepository: TagRepository
) {
    fun findByTitle(title: String): Match? = matchRepository.findByTitle(title)

    fun findById(id: Long): Match = matchRepository.findById(id)
        .orElseThrow { throw CustomExceptions.MatchNotFoundException("해당 id의 매치를 찾지 못했습니다") }

    fun findByIdIn(ids: List<Long>): List<Match> = matchRepository.findByIdIn(ids).toList()

    fun create(
        title: String,
        thumbnailUrl: String,
        icon: String,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?,
        tags: MutableSet<Long>
    ): Match {
        val match: Match = Match.of(
            title,
            icon,
            thumbnailUrl,
            startAt,
            endAt
        )
        tags.stream().forEach {
            val tag = tagRepository.findById(it)
                .orElseThrow { throw CustomExceptions.CreateMatchException("해당 id의 태그를 찾지 못했습니다") }
            val matchAndTag = MatchAndTag(match, tag)
            match.matchAndTags.add(matchAndTag)
        }
        return matchRepository.save(match)
    }

    fun findAllMatchOrderByCreatedAtDesc(): List<Match> = matchRepository.findAllByOrderByCreatedAtDesc().toList()

    fun findAllMatchOrderByTitleDesc(): List<Match> = matchRepository.findAllByOrderByTitleDesc().toList()

    fun findAllMatch(pageable: Pageable): Page<Match> = matchRepository.findAll(pageable)
}
