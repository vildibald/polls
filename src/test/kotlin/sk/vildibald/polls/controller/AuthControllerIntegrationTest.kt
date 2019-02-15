package sk.vildibald.polls.controller

import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sk.vildibald.polls.IntegrationTest
import sk.vildibald.polls.payload.LoginRequest
import sk.vildibald.polls.payload.SignUpRequest
import sk.vildibald.polls.toJson

@IntegrationTest
@RunWith(SpringRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
internal class AuthControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

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

    private val signInUrl = "/api/auth/signin"
    private val signUpUrl = "/api/auth/signup"

    @Test
    fun `Test 1 - Register user`() {
        mockMvc.perform(post(signUpUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testingSignUpRequest.toJson())
        ).andExpect(status().is2xxSuccessful)
    }

    @Test
    fun `Test 2 - Fail to register user`() {
        mockMvc.perform(post(signUpUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testingSignUpRequest.toJson())
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `Test 3 - Authenticate user`() {
        mockMvc.perform(post(signInUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testingSignInRequest.toJson())
        ).andExpect(status().isOk)
    }
}