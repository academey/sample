package com.example.academey.controllers.dto

import com.example.academey.domain.match.Tag
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

class TagDto {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class TagRes(
        val id: Long,
        val name: String
    ) {
        companion object {
            fun of(tag: Tag) = TagRes(
                tag.id,
                tag.name
            )
        }
    }
}
