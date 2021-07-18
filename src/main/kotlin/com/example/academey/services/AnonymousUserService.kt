package com.example.academey.services

import com.example.academey.domain.user.AnonymousUser
import com.example.academey.domain.user.AnonymousUserName
import com.example.academey.repositories.AnonymousUserNameRepository
import com.example.academey.repositories.AnonymousUserRepository
import com.example.academey.utils.logger.UpsideLogger
import org.springframework.stereotype.Service
import java.util.Random

@Service
class AnonymousUserService(
    private val anonymousUserRepository: AnonymousUserRepository,
    private val anonymousUserNameRepository: AnonymousUserNameRepository
) {
    fun findByUid(uid: String): AnonymousUser? = anonymousUserRepository.findByUid(uid)

    fun signUpIfNotExist(
        uid: String
    ): AnonymousUser {
        val findByUid = findByUid(uid)
        if (findByUid != null) {
            return findByUid
        }

        return create(
            uid
        )
    }

    fun create(
        uid: String
    ): AnonymousUser {
        var anonymousUserName = findAnonymousUserName()
        var name = anonymousUserName.name + anonymousUserName.seq

        return anonymousUserRepository.save(
            AnonymousUser(
                uid, name
            )
        )
    }

    fun findAll(): List<AnonymousUser>? = anonymousUserRepository.findAll().toList()

    companion object : UpsideLogger()

    fun findAnonymousUserName(): AnonymousUserName {
        val random = Random()
        val nameCount = anonymousUserNameRepository.count()
        val nameId = random.nextInt(nameCount.toInt()) + 1
        val anonymousUserName = anonymousUserNameRepository.findById(nameId.toLong()).get()

        return anonymousUserNameRepository.save(anonymousUserName.plusSeq())
    }
}
