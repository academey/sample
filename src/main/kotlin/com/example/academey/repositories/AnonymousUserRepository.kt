package com.example.academey.repositories

import com.example.academey.domain.user.AnonymousUser
import org.springframework.data.repository.CrudRepository

interface AnonymousUserRepository : CrudRepository<AnonymousUser, Long> {
    fun findByUid(uid: String): AnonymousUser?
    fun findByName(name: String): AnonymousUser?
    fun findCountByName(name: String): Long
}
