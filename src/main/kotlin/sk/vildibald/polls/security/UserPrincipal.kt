package sk.vildibald.polls.security

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import sk.vildibald.polls.model.User
import java.util.*

class UserPrincipal(
        val id: Long,

        val name: String,

        private val username: String,

        @JsonIgnore
        val email: String,

        @JsonIgnore
        private val password: String,

        private val authorities: MutableCollection<out GrantedAuthority>
): UserDetails {
    constructor(user: User): this(
            id = user.id,
            name = user.name,
            username = user.username,
            email = user.email,
            password = user.password,
            authorities = user.roles.map { SimpleGrantedAuthority(it.name.name) }.toMutableList()
    )

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as UserPrincipal?
        return id == that?.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}

