package com.example.academey.domain.match

import com.example.academey.domain.BaseTimeEntity
import com.fasterxml.jackson.annotation.JsonProperty
import org.modelmapper.ModelMapper
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.OneToMany
import javax.persistence.FetchType

// TODO : 각 속성 별 VALIDATION 명확히 설정.
@Entity
@Table(name = "match", schema = "public")
class Match(
    var title: String,
    var icon: String,
    var thumbnailUrl: String,
    var startAt: LocalDateTime? = null,
    var endAt: LocalDateTime? = null,
    var isDeleted: Boolean = false,
    @OneToMany(mappedBy = "match", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
    @JsonProperty("tags")
    var matchAndTags: MutableSet<MatchAndTag> = mutableSetOf(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
) : BaseTimeEntity() {
    companion object {
        fun of(
            title: String,
            icon: String,
            thumbnailUrl: String,
            startAt: LocalDateTime?,
            endAt: LocalDateTime?
        ): Match {
            val modelMapper = ModelMapper()
            val matchEntity: Match = modelMapper.map(
                mapOf(
                    "title" to title,
                    "icon" to icon,
                    "thumbnailUrl" to thumbnailUrl,
                    "startAt" to startAt,
                    "endAt" to endAt
                ),
                Match::class.java
            )
            matchEntity.matchAndTags = mutableSetOf()
            return matchEntity
        }
    }
}
