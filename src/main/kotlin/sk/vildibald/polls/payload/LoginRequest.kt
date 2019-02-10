package sk.vildibald.polls.payload

data class LoginRequest(val usernameOrEmail: String, val password: String)