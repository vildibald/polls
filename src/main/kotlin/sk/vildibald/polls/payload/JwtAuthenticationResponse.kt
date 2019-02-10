package sk.vildibald.polls.payload

data class JwtAuthenticationResponse(
        val accessToken: String,
        val tokenType: String = "Bearer"
)