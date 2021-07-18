package com.example.academey.controllers.dto

import com.example.academey.domain.user.AnonymousUser

class AnonymousUserDto {
    data class AnonymousUserRes(
        val id: Long,
        val uid: String,
        val name: String
    ) {
        companion object {
            fun of(anonymousUser: AnonymousUser) = AnonymousUserRes(
                anonymousUser.id,
                anonymousUser.uid,
                anonymousUser.name
            )
        }
    }
}
