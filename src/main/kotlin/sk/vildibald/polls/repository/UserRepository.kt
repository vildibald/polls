package sk.vildibald.polls.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sk.vildibald.polls.model.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findOneByUsername(username: String): User?

    fun findOneByEmail(email: String): User?

    fun findOneByUsernameOrEmail(username: String, email: String): User?

    fun findByIdIn(userIds: Iterable<Long>): List<User>

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}