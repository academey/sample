package com.example.academey.services

import com.example.academey.domain.user.Password
import com.example.academey.domain.user.Role
import com.example.academey.domain.user.User
import com.example.academey.domain.user.UserFollow
import com.example.academey.repositories.UserFollowRepository
import com.example.academey.repositories.UserRepository
import com.example.academey.utils.CustomExceptions
import com.example.academey.utils.logger.UpsideLogger
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userFollowRepository: UserFollowRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun findById(id: Long): User? = userRepository.findById(id).orElse(null)
    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

    fun findAll(): Iterable<User> = userRepository.findAll()

    fun signUp(
        email: String,
        name: String,
        picture: String?,
        rawPassword: String
    ): User {
        log.debug("Some message!")
        if (userRepository.findByEmail(email) != null) {
            throw CustomExceptions.UserExistException()
        }

        return create(
            email,
            name,
            picture,
            rawPassword
        )
    }

    fun signIn(email: String, rawPassword: String): User {
        val user: User = findByEmail(email) ?: throw CustomExceptions.UserNotFoundException()
        require(user.password.isMatched(rawPassword = rawPassword, passwordEncoder = passwordEncoder)) {
            throw CustomExceptions.EmailSignInFailedException()
        }

        return user
    }

    fun create(
        email: String?,
        name: String,
        picture: String?,
        rawPassword: String?
    ): User = userRepository.save(
        User(
            email,
            name,
            picture,
            Role.USER, // 최초 가입시 USER 로 설정
            Password(rawPassword?.let { passwordEncoder.encode(it) })
        )
    )

    fun update(
        user: User,
        name: String?,
        picture: String?
    ): User =
        userRepository.save(
            user.update(
                name,
                picture
            )
        )

    fun updateUserNickName(
        user: User,
        name: String
    ): User {
        if (isDuplicateName(name)) {
            throw CustomExceptions.UserNickNameDuplicatedException()
        }
        return userRepository.save(
            user.update(name)
        )
    }

    fun isDuplicateName(name: String): Boolean {
        return userRepository.findByName(name) != null
    }

    fun signOut(user: User) = userRepository.delete(user)

    fun follow(
        user: User,
        followedUser: User
    ): UserFollow {
        if (user.id == followedUser.id) throw CustomExceptions.BadRequestException("자신을 pick 할수는 없습니다")
        val userPick = userFollowRepository.findByUserIdAndFollowedUserId(
            user.id,
            followedUser.id
        )

        return if (userPick == null) {
            userFollowRepository.save(
                UserFollow(
                    user = user,
                    followedUser = followedUser
                )
            ).also {
                it.user.pickCount += 1
                userRepository.save(it.user)
            }
        } else {
            userPick.also {
                it.isPicked = !it.isPicked
                if (it.isPicked) {
                    it.user.pickCount += 1
                } else {
                    it.user.pickCount -= 1
                }
                userFollowRepository.save(it)
            }
        }
    }

    companion object : UpsideLogger()
}
