package com.example.academey.domain.user

enum class SocialProvider(
    val key: String,
    val title: String
) {
    GOOGLE("google", "구글"),
    KAKAO("kakao", "카카오"),
    APPLE("apple", "애플")
}
