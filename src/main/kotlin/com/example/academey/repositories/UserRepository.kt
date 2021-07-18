package com.example.academey.repositories

import com.example.academey.domain.user.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByName(name: String): User?
}
