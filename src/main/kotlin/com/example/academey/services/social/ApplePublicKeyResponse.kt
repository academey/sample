package com.example.academey.services.social

data class ApplePublicKeyResponse(
    val keys: List<Key>
) {
    data class Key(
        val kty: String? = null,
        val kid: String? = null,
        val use: String? = null,
        val alg: String? = null,
        val n: String? = null,
        val e: String? = null
    )

    fun getMatchedKeyBy(kid: String?, alg: String?): Key? {
        return keys.stream()
            .filter { key: Key ->
                key.kid.equals(kid) && key.alg.equals(alg)
            }
            .findFirst().orElse(null)
    }
}
