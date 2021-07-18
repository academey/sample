package com.example.academey.repositories

import com.example.academey.domain.user.OAuthProvider
import org.springframework.data.repository.CrudRepository

interface OAuthProviderRepository : CrudRepository<OAuthProvider, Long> {
    fun findByUid(uid: String): OAuthProvider?
}
