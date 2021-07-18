package com.example.academey.repositories

import com.example.academey.domain.user.UserFollow
import org.springframework.data.repository.CrudRepository

interface UserFollowRepository : CrudRepository<UserFollow, Long> {
    fun findAllByUserId(userId: Long): List<UserFollow>
    fun findByUserIdAndFollowedUserId(userId: Long, followedUserId: Long): UserFollow?
}
