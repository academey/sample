package com.example.academey.domain.user

enum class Role(
    val key: String,
    val title: String
) {
    GUEST("GUEST", "손님"),
    ADMIN("ADMIN", "관리자"),
    USER("USER", "일반 사용자")
}
