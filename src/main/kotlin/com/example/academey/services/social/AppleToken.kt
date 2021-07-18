package com.example.academey.services.social

class AppleToken {
    data class Request(
        var code: String? = null,
        var client_id: String? = null,
        var client_secret: String? = null,
        var grant_type: String? = null,
        var refresh_token: String? = null
    ) {
        companion object {
            fun of(
                code: String?,
                clientId: String?,
                clientSecret: String?,
                grantType: String?,
                refreshToken: String?
            ): Request {
                val request = Request()
                request.code = code
                request.client_id = clientId
                request.client_secret = clientSecret
                request.grant_type = grantType
                request.refresh_token = refreshToken
                return request
            }
        }
    }

    data class Response(
        val accessToken: String? = null,
        val expiresIn: String? = null,
        val idToken: String? = null,
        val refreshToken: String? = null,
        val tokenType: String? = null,
        val error: String? = null
    )
}
