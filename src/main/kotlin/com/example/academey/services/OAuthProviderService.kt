package com.example.academey.services

import com.example.academey.domain.user.OAuthProvider
import com.example.academey.domain.user.SocialProvider
import com.example.academey.domain.user.User
import com.example.academey.repositories.OAuthProviderRepository
import com.example.academey.utils.logger.UpsideLogger
import org.springframework.stereotype.Service

@Service
class OAuthProviderService(
    private val oAuthProviderRepository: OAuthProviderRepository
) {
    fun findByUid(uid: String): OAuthProvider? = oAuthProviderRepository.findByUid(uid)

    fun create(
        user: User,
        provider: SocialProvider,
        uid: String
    ): OAuthProvider = oAuthProviderRepository.save(
        OAuthProvider(
            user,
            provider,
            uid
        )
    )

    companion object : UpsideLogger()
}
