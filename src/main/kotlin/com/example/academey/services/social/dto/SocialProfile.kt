package com.example.academey.services.social.dto

data class SocialProfile(
    val uid: String,
    val email: String?,
    val picture: String?
) {
    companion object {
        fun ofGoogle(profile: GoogleProfile) =
            SocialProfile(
                profile.sub,
                null,
                profile.picture
            )

        fun ofKakao(profile: KakaoProfile) =
            SocialProfile(
                profile.id.toString(),
                null,
                profile.properties.profileImage
            )
    }
}
