package sk.vildibald.polls.security

import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenProvider{
    val logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    @Value("\${app.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${app.jwtExpirationInMs}")
    private val jwtExpirationInMs: Int = 0

    fun generateToken(authentication: Authentication): String{
        val userPrincipal = authentication.principal as UserPrincipal

        val issuedAt = Date()
        val expiredIn = Date(issuedAt.time + jwtExpirationInMs)

        return Jwts.builder()
                .setSubject(userPrincipal.id.toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredIn)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun userIdFromJwt(token: String): Long{
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .body
                .subject.toLong()
    }

    fun validateToken(token: String): Boolean{
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }

        return false
    }
}