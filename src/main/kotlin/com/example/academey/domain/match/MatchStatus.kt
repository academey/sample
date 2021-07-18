package com.example.academey.domain.match

enum class MatchStatus(
    val key: String,
    val title: String
) {
    NEEDED_TO_PARTICIPATE("NEEDED_TO_PARTICIPATE", "출전 필요"),
    AVAILABLE_TO_START("AVAILABLE_TO_START", "시작 가능")
}
