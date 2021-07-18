package com.example.academey.services.social.dto

import com.example.academey.services.social.AppleToken

data class SocialAuth(
    val accessToken: String,
    val idToken: String? = null
) {
    companion object {
        fun ofGoogle(auth: GoogleAuth) =
            SocialAuth(
                auth.accessToken
            )

        fun ofKakao(auth: KakaoAuth) =
            SocialAuth(
                auth.accessToken
            )

        fun ofApple(auth: AppleToken.Response) =
            SocialAuth(
                auth.accessToken!!,
                auth.idToken!!
            )
    }
}
