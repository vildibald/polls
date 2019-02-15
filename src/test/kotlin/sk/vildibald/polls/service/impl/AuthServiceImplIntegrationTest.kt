package sk.vildibald.polls.service.impl

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.test.context.junit4.SpringRunner
import sk.vildibald.polls.IntegrationTest
import sk.vildibald.polls.payload.LoginRequest
import sk.vildibald.polls.payload.SignUpRequest
import sk.vildibald.polls.service.AuthService

@IntegrationTest
@RunWith(SpringRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
internal class AuthServiceImplIntegrationTest {

    @Autowired
    private lateinit var authService: AuthService

    private val testingSignUpRequest = SignUpRequest(
            name = "Fero Traktorista",
            username = "feri007",
            email = "feri007@jrd.sk",
            password = "123456"
    )

    private val testingSignInRequest = LoginRequest(
            usernameOrEmail = "feri007",
            password = "123456"
    )

    @Test
    fun `Test 1 - Register new user`() {
        val response = authService.registerUser(testingSignUpRequest)
        assertTrue(response.success)
    }

    @Test
    fun `Test 2 - Fail to register new user with existing email`() {
        val response = authService.registerUser(testingSignUpRequest)
        assertFalse(response.success)
    }

    @Test
    fun `Test 3 - Authenticate user`() {
        authService.authenticateUser(testingSignInRequest)
    }

    @Test
    fun `Test 4 - Fail to authenticate user with bad username or email`() {
        assertThrows<BadCredentialsException> {
            authService.authenticateUser(testingSignInRequest
                    .copy(usernameOrEmail = "planyUsername"))
        }
    }

    @Test
    fun `Test 5 - Fail to authenticate user with bad password`() {
        assertThrows<BadCredentialsException> {
            authService.authenticateUser(testingSignInRequest
                    .copy(password = "planeHeslo"))
        }
    }
}
