package sk.vildibald.polls.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import sk.vildibald.polls.payload.LoginRequest
import sk.vildibald.polls.payload.SignUpRequest
import sk.vildibald.polls.service.AuthService
import javax.validation.Valid


@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val authService: AuthService
) {
    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> =
            ResponseEntity.ok(authService.authenticateUser(loginRequest))

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> {
        val response = authService.registerUser(signUpRequest)
        return if (response.success.not()) {
            ResponseEntity(response, HttpStatus.BAD_REQUEST)
        } else {
            val location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/users/{username}")
                    .buildAndExpand(signUpRequest.username).toUri()
            ResponseEntity.created(location).body(response)
        }
    }
}