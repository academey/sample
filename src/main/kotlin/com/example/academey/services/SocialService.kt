package com.example.academey.services

import com.example.academey.domain.user.SocialProvider
import com.example.academey.domain.user.User
import com.example.academey.services.social.AppleService
import com.example.academey.services.social.GoogleService
import com.example.academey.services.social.KakaoService
import com.example.academey.services.social.dto.SocialAuth
import com.example.academey.utils.CustomExceptions
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class SocialService(
    private val kakaoService: KakaoService,
    private val googleService: GoogleService,
    private val appleService: AppleService,
    private val oAuthProviderService: OAuthProviderService,
    private val userService: UserService
) {
    @Transactional
    fun getLoginUrl(
        provider: String,
        currentHost: String
    ): StringBuilder = when (provider) {
        SocialProvider.KAKAO.key -> kakaoService.getLoginUrl(currentHost)
        SocialProvider.GOOGLE.key -> googleService.getLoginUrl(currentHost)
        SocialProvider.APPLE.key -> appleService.getLoginUrl(currentHost)
        else -> throw CustomExceptions.ProviderNotFoundException("그런 소셜 프로바이더가 없습니다 $provider")
    }

    @Transactional
    fun getTokenInfo(
        provider: String,
        currentHost: String,
        code: String
    ): SocialAuth =
        when (provider) {
            SocialProvider.KAKAO.key -> kakaoService.getTokenInfo(currentHost, code)
            SocialProvider.GOOGLE.key -> googleService.getTokenInfo(currentHost, code)
            SocialProvider.APPLE.key -> appleService.getTokenInfo(currentHost, code)
            else -> throw CustomExceptions.ProviderNotFoundException("그런 소셜 프로바이더가 없습니다 $provider")
        }

    @Transactional
    fun socialSignUp(
        provider: String,
        accessToken: String?,
        idToken: String?,
        name: String,
        userPictureImage: String?,
        description: String?,
        instagramName: String?
    ): User {
        when (provider) {
            SocialProvider.KAKAO.key ->
                kakaoService.getProfile(
                    accessToken
                        ?: throw CustomExceptions.BadRequestException("$provider social login 시 access_token 이 있어야 합니다.")
                )
            SocialProvider.GOOGLE.key ->
                googleService.getProfile(
                    accessToken
                        ?: throw CustomExceptions.BadRequestException("$provider social login 시 access_token 이 있어야 합니다.")
                )
            SocialProvider.APPLE.key ->
                appleService.getProfile(
                    idToken
                        ?: throw CustomExceptions.BadRequestException("$provider social login 시 id_token 이 있어야 합니다.")
                )
            else -> throw CustomExceptions.ProviderNotFoundException("그런 소셜 프로바이더가 없습니다 $provider")
        }.let { (uid, email, picture) ->
            val oAuthProvider = oAuthProviderService.findByUid(uid)
            if (oAuthProvider != null) {
                throw CustomExceptions.UserExistException("이미 해당 소셜 계정으로 가입했습니다 $uid, $name, $email")
            }
            if (userService.isDuplicateName(name)) {
                throw CustomExceptions.UserNickNameDuplicatedException()
            }

            val user = userService.create(
                email = email,
                name = name,
                picture = userPictureImage.let {
                    userPictureImage ?: picture
                },
                rawPassword = null
            )
            oAuthProviderService.create(
                user,
                SocialProvider.valueOf(provider.toUpperCase()),
                uid
            )
            return user
        }
    }

    @Transactional
    fun socialSignInOrSignUp(
        provider: String,
        accessToken: String?,
        idToken: String?,
        name: String,
        picture: String?,
        description: String?,
        instagramName: String?
    ): Pair<ResponseService.ResponseCode, User> {
        return try {
            Pair(ResponseService.ResponseCode.SOCIAL_SIGN_IN, socialSignIn(provider, accessToken, idToken))
        } catch (e: CustomExceptions.UserNotFoundException) {
            Pair(
                ResponseService.ResponseCode.SOCIAL_SIGN_UP,
                socialSignUp(provider, accessToken, idToken, name, picture, description, instagramName)
            )
        }
    }

    fun socialSignIn(
        provider: String,
        accessToken: String?,
        idToken: String?
    ): User {
        when (provider) {
            SocialProvider.KAKAO.key -> kakaoService.getProfile(
                accessToken
                    ?: throw CustomExceptions.BadRequestException("$provider social login 시 access_token 이 있어야 합니다.")
            )
            SocialProvider.GOOGLE.key -> googleService.getProfile(
                accessToken
                    ?: throw CustomExceptions.BadRequestException("$provider social login 시 access_token 이 있어야 합니다.")
            )
            SocialProvider.APPLE.key -> appleService.getProfile(
                idToken ?: throw CustomExceptions.BadRequestException("$provider social login 시 id_token 이 있어야 합니다.")
            )
            else -> throw CustomExceptions.ProviderNotFoundException("그런 소셜 프로바이더가 없습니다 $provider")
        }.let { (uid) ->
            val oAuthProvider = oAuthProviderService.findByUid(uid) ?: throw CustomExceptions.UserNotFoundException()
            return oAuthProvider.user
        }
    }
}
