package sk.vildibald.polls.controller

import org.springframework.http.ResponseEntity
import sk.vildibald.polls.payload.LoginRequest
import sk.vildibald.polls.payload.SignUpRequest

interface AuthController{
    fun authenticateUser(loginRequest: LoginRequest): ResponseEntity<*>

    fun registerUser(signUpRequest: SignUpRequest): ResponseEntity<*>
}