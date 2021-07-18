package com.example.academey.controllers.dto

import com.example.academey.domain.match.Match
import com.example.academey.domain.match.Tag
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

class MatchDto {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class CreateMatchReq(
        @field:NotEmpty
        val title: String? = null,
        @field:NotEmpty
        val icon: String? = null,
        // field:NotEmpty 가 안 먹음. SnakeCase 도 안 먹음.
        var thumbnail_image: MultipartFile? = null,
        val startAt: LocalDateTime? = null,
        val endAt: LocalDateTime?,
        @field:NotEmpty
        val tags: MutableSet<Long>? = null
    )

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class CompleteMatchReq(
        @field:NotEmpty
        @field:Size(min = 15, max = 15)
        val historyList: MutableList<MatchResult>? = null
    ) {
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
        data class MatchResult(
            val winnerPetPhotoId: Long,
            val loserPetPhotoId: Long,
            val step: Int
        )
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class MatchRes(
        val id: Long,
        val title: String,
        val icon: String,
        val thumbnailUrl: String,
        val startAt: LocalDateTime?,
        val endAt: LocalDateTime?,
        val tags: List<Tag>
    ) {
        companion object {
            fun of(match: Match) = MatchRes(
                match.id,
                match.title,
                match.icon,
                match.thumbnailUrl,
                match.startAt,
                match.endAt,
                match.matchAndTags.map {
                    it.tag
                }
            )
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class MatchWithStatusRes(
        val id: Long,
        val title: String,
        val icon: String,
        val thumbnailUrl: String,
        val startAt: LocalDateTime?,
        val endAt: LocalDateTime?,
        val tags: List<Tag>
    ) {
        companion object {
            fun of(match: Match) = MatchWithStatusRes(
                match.id,
                match.title,
                match.icon,
                match.thumbnailUrl,
                match.startAt,
                match.endAt,
                match.matchAndTags.map {
                    it.tag
                }
            )
        }
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class MatchWithParticipantsRes(
        val id: Long,
        val title: String,
        val icon: String,
        val thumbnailUrl: String,
        val startAt: LocalDateTime?,
        val endAt: LocalDateTime?,
        val tags: List<Tag>
    ) {
        companion object {
            fun of(match: Match) = MatchWithParticipantsRes(
                match.id,
                match.title,
                match.icon,
                match.thumbnailUrl,
                match.startAt,
                match.endAt,
                match.matchAndTags.map {
                    it.tag
                }
            )
        }
    }
}
