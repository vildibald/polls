package sk.vildibald.polls.service.impl

import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import sk.vildibald.polls.UnitTest
import sk.vildibald.polls.model.Role
import sk.vildibald.polls.model.RoleName
import sk.vildibald.polls.model.User
import sk.vildibald.polls.payload.LoginRequest
import sk.vildibald.polls.payload.SignUpRequest
import sk.vildibald.polls.repository.RoleRepository
import sk.vildibald.polls.repository.UserRepository
import sk.vildibald.polls.service.AuthService

@UnitTest
@RunWith(SpringRunner::class)
internal class AuthServiceImplTest {

    @Autowired
    private lateinit var authService: AuthService

    @MockBean
    protected lateinit var userRepository: UserRepository

    @MockBean
    protected lateinit var roleRepository: RoleRepository

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder

    @Before
    fun commonMocks() {
        whenever(roleRepository.findOneByName(RoleName.ROLE_USER)) doReturn
                Role(1, RoleName.ROLE_USER)

        whenever(userRepository.save(any<User>())) doAnswer { it.arguments[0] as User }
        whenever(userRepository.findOneByUsernameOrEmail(testingSignInRequest.usernameOrEmail,
                testingSignInRequest.usernameOrEmail)
        ) doReturn testingUser

        whenever(passwordEncoder.encode(anyString())) doAnswer { it.arguments[0] as String }
        whenever(passwordEncoder.matches(anyString(), anyString())) doReturn true

    }

    private val testingUser = User(
            name = "Fero Traktorista",
            username = "feri007",
            email = "feri007@jrd.sk",
            password = "123456",
            roles = setOf(Role(1, RoleName.ROLE_USER)))

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
    fun `Authenticate user`() {
        authService.authenticateUser(testingSignInRequest)
    }

    @Test
    fun `Fail to authenticate user with bad username or email`() {
        whenever(userRepository.findOneByUsernameOrEmail(testingSignInRequest.usernameOrEmail,
                testingSignInRequest.usernameOrEmail)
        ).doReturn<User?>(null)

        assertThrows<BadCredentialsException> { authService.authenticateUser(testingSignInRequest) }
    }

    @Test
    fun `Fail to authenticate user with bad password`() {
        whenever(passwordEncoder.matches(anyString(), anyString())) doReturn false

        assertThrows<BadCredentialsException> { authService.authenticateUser(testingSignInRequest) }
    }

    @Test
    fun `Register new user`() {
        whenever(userRepository.existsByEmail(anyString())) doReturn false
        whenever(userRepository.existsByUsername(anyString())) doReturn false

        val response = authService.registerUser(testingSignUpRequest)
        assertTrue(response.success)
    }

    @Test
    fun `Fail to register new user with existing email`() {
        whenever(userRepository.existsByEmail(anyString())) doReturn true
        whenever(userRepository.existsByUsername(anyString())) doReturn false

        val response = authService.registerUser(testingSignUpRequest)
        assertFalse(response.success)
    }

    @Test
    fun `Fail to register new user with existing username`() {
        whenever(userRepository.existsByEmail(anyString())) doReturn false
        whenever(userRepository.existsByUsername(anyString())) doReturn true

        val response = authService.registerUser(testingSignUpRequest)
        assertFalse(response.success)
    }


}

//package sk.vildibald.polls.service.impl
//
//import com.ninjasquad.springmockk.MockkBean
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.Assert.assertFalse
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Test
//import org.junit.experimental.categories.Category
//import org.junit.jupiter.api.extension.ExtendWith
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.context.TestConfiguration
//import org.springframework.context.annotation.Bean
//import org.springframework.test.context.junit.jupiter.SpringExtension
//import org.springframework.test.context.junit4.SpringRunner
//import sk.vildibald.polls.UnitTest
//import sk.vildibald.polls.model.Role
//import sk.vildibald.polls.model.RoleName
//import sk.vildibald.polls.model.User
//import sk.vildibald.polls.payload.SignUpRequest
//import sk.vildibald.polls.repository.RoleRepository
//import sk.vildibald.polls.repository.UserRepository
//import sk.vildibald.polls.service.AuthService
//
//@RunWith(SpringRunner::class)
//@ExtendWith(SpringExtension::class)
//@SpringBootTest
//@Category(UnitTest::class)
//internal class AuthServiceImplTest {
//
//    @Autowired
//    private lateinit var authService: AuthService
//
//    @Autowired
//    private lateinit var userRepository: UserRepository
//
//    @Autowired
//    private lateinit var roleRepository: RoleRepository
//
//    @TestConfiguration
//    class TestConfig {
//        @Bean
//        fun mockUserRepository() = mockk<UserRepository>(relaxed = true)
//
//        @Bean
//        fun mockRoleRepository() = mockk<RoleRepository>(relaxed = true)
//    }
//
//    @Before
//    fun commonMocks() {
//        every { userRepository.save(any<User>()) } returns testUser()
//        every { roleRepository.findOneByName(RoleName.ROLE_USER) } returns
//                Role(1, RoleName.ROLE_USER)
//    }
//
//    private fun testUser() = User(
//            name = "Fero Traktorista",
//            username = "feri007",
//            email = "feri007@jrd.sk",
//            password = "123456",
//            roles = setOf(Role(1, RoleName.ROLE_USER)))
//
//    private fun testSignUpRequest() = SignUpRequest(
//            name = "Fero Traktorista",
//            username = "feri007",
//            email = "feri007@jrd.sk",
//            password = "123456"
//    )
//
//    @Test
//    fun authenticateUser() {
//    }
//
//    @Test
//    fun `Register new user`() {
//        every { userRepository.existsByEmail(any()) } returns false
//        every { userRepository.existsByUsername(any()) } returns false
//
//        val response = authService.registerUser(testSignUpRequest())
//        assertTrue(response.success)
//    }
//
//    @Test
//    fun `Fail to register new user with existing email`() {
//        every { userRepository.existsByEmail(any()) } returns true
//        every { userRepository.existsByUsername(any()) } returns false
//
//        val response = authService.registerUser(testSignUpRequest())
//        assertFalse(response.success)
//    }
//
//    @Test
//    fun `Fail to register new user with existing username`() {
//        every { userRepository.existsByEmail(any()) } returns false
//        every { userRepository.existsByUsername(any()) } returns true
//
//        val response = authService.registerUser(testSignUpRequest())
//        assertFalse(response.success)
//    }
//
//
//}