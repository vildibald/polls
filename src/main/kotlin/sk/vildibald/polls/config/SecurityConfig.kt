package sk.vildibald.polls.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import sk.vildibald.polls.security.JwtAuthenticationEntryPoint
import sk.vildibald.polls.security.JwtAuthenticationFilter
import sk.vildibald.polls.security.JwtTokenProvider
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import sk.vildibald.polls.service.impl.UserDetailsServiceImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds




@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
class SecurityConfig(
        private val userDetailsService: UserDetailsServiceImpl,
        private val unauthorizedHandler: JwtAuthenticationEntryPoint,
        private val tokenProvider: JwtTokenProvider
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter =
            JwtAuthenticationFilter(tokenProvider, userDetailsService)

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager =
            super.authenticationManagerBean()

    @Bean
    fun passwordEncoder(): PasswordEncoder =
            BCryptPasswordEncoder()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.cors()
                .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                        .permitAll()
                    .antMatchers("/hello", "/api/auth/**")
                        .permitAll()
                    .antMatchers("/api/user/checkUsernameAvailability",
                            "/api/user/checkEmailAvailability")
                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated()

        http.addFilterBefore(jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter::class.java)
    }
}