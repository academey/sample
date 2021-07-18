package com.example.academey.services.social

import com.example.academey.services.social.dto.SocialAuth
import com.example.academey.services.social.dto.SocialProfile

interface BaseSocialService {
    fun getProfile(accessToken: String): SocialProfile
    fun getTokenInfo(currentHost: String, code: String): SocialAuth
    fun getLoginUrl(currentHost: String): StringBuilder
}
