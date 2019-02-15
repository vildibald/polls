package sk.vildibald.polls.controller

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sk.vildibald.polls.UnitTest
import sk.vildibald.polls.payload.ApiResponse
import sk.vildibald.polls.payload.JwtAuthenticationResponse
import sk.vildibald.polls.payload.LoginRequest
import sk.vildibald.polls.payload.SignUpRequest
import sk.vildibald.polls.service.AuthService
import sk.vildibald.polls.toJson

@UnitTest
@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
internal class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
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

    private val signInUrl = "/api/auth/signin"
    private val signUpUrl = "/api/auth/signup"

    @Test
    fun `Authenticate user`() {
        val serviceResponse = JwtAuthenticationResponse("testToken")

        doReturn(serviceResponse).whenever(authService).authenticateUser(testingSignInRequest)

        mockMvc.perform(post(signInUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testingSignInRequest.toJson())
        ).andExpect(status().isOk)

    }

    @Test
    fun `Register user`() {
        val serviceResponse = ApiResponse(true, "")

        doReturn(serviceResponse).whenever(authService).registerUser(testingSignUpRequest)

        mockMvc.perform(post(signUpUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testingSignUpRequest.toJson())
        ).andExpect(status().is2xxSuccessful)
        //.andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
        //.andReturn()
    }

    @Test
    fun `Fail to register user`() {
        val serviceResponse = ApiResponse(false, "")

        doReturn(serviceResponse).whenever(authService).registerUser(testingSignUpRequest)

        mockMvc.perform(post(signUpUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testingSignUpRequest.toJson())
        ).andExpect(status().isBadRequest)
    }
}