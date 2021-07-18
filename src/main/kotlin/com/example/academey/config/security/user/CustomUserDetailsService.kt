package com.example.academey.config.security.user

import com.example.academey.services.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userService: UserService
) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userPk: String): UserDetails {
        return userService.findById(userPk.toLong()) ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다.")
    }
}
