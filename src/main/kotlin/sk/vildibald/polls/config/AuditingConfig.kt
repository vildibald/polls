package sk.vildibald.polls.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import sk.vildibald.polls.security.UserPrincipal
import java.util.*

/**
 * This configuration defines auditor.
 */
@Configuration
@EnableJpaAuditing
class AuditingConfig {
    @Bean
    fun auditorProvider(): AuditorAware<Long> =
            SpringSecurityAuditAwareImpl()
}

/**
 * This [AuditorAware] implementation will determine if current user is authenticated.
 */
internal class SpringSecurityAuditAwareImpl : AuditorAware<Long> {
    override fun getCurrentAuditor(): Optional<Long> {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication == null ||
                authentication.isAuthenticated.not() ||
                authentication is AnonymousAuthenticationToken)
            Optional.empty()
        else
            Optional.ofNullable((authentication.principal as UserPrincipal).id)
    }
}