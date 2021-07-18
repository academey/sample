package com.example.academey.controllers

import com.example.academey.config.security.JwtTokenProvider
import com.example.academey.domain.match.Match
import com.example.academey.domain.match.MatchAndTag
import com.example.academey.domain.match.Tag
import com.example.academey.services.MatchService
import com.example.academey.services.UserService
import com.example.academey.utils.IntegrationTest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@IntegrationTest
class MatchApiControllerTest(
    private val mockMvc: MockMvc,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var matchService: MatchService

    @Test
    fun `List Matches`() {
        val testTag = Tag(
            "귀여움",
            mutableSetOf()
        )

        val testMatch = Match(
            "title",
            "subtitle",
            "thumbnailUrl"
        )

        val testMatchAndTag = MatchAndTag(
            testMatch,
            testTag
        )

        testMatch.matchAndTags.add(testMatchAndTag)
        testTag.matchAndTags.add(testMatchAndTag)

        val pageable: Pageable = PageRequest.of(0, 1, Sort.by("title"))
        every {
            matchService.findAllMatch(pageable)
        } returns PageImpl(listOf(testMatch))
    }
}
