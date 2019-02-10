package sk.vildibald.polls.service

import sk.vildibald.polls.payload.ApiResponse
import sk.vildibald.polls.payload.JwtAuthenticationResponse
import sk.vildibald.polls.payload.LoginRequest
import sk.vildibald.polls.payload.SignUpRequest

interface AuthService {
    fun authenticateUser(loginRequest: LoginRequest): JwtAuthenticationResponse

    fun registerUser(signUpRequest: SignUpRequest): ApiResponse
}