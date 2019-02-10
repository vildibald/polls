package sk.vildibald.polls.service.impl

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.vildibald.polls.exception.ResourceNotFoundException
import sk.vildibald.polls.repository.UserRepository
import sk.vildibald.polls.security.UserPrincipal

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) :
        UserDetailsService {

    @Transactional
    override fun loadUserByUsername(usernameOrEmail: String): UserDetails {
        val user = userRepository.findOneByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                ?: throw UsernameNotFoundException("User not found with username or email: $usernameOrEmail")
        return UserPrincipal(user)
    }

    @Transactional
    fun loadUserById(id: Long): UserPrincipal {
        val user = userRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("User", "id", id) }
        return UserPrincipal(user)
    }
}