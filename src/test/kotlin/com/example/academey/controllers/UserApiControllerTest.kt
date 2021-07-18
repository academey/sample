package com.example.academey.controllers

import com.example.academey.utils.IntegrationTest
import com.example.academey.config.security.JwtTokenProvider
import com.example.academey.config.security.JwtTokenProvider.Companion.AUTHORIZATION_HEADER_KEY
import com.example.academey.domain.user.Password
import com.example.academey.domain.user.Role
import com.example.academey.domain.user.User
import com.example.academey.services.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@IntegrationTest
class UserApiControllerTest(
    private val mockMvc: MockMvc,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @MockkBean
    private lateinit var userService: UserService

    @Test
    fun `Get User Profile with security token`() {
        val juergen = User(
            "springjuergen",
            "name",
            "picture",
            Role.USER,
            Password("password")
        )
        every {
            userService.findById(juergen.id)
        } returns juergen
        val token = jwtTokenProvider.createToken(juergen.id, juergen.role)

        mockMvc.perform(
            get("/api/users/me").accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
    }
}
