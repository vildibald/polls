package sk.vildibald.polls.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sk.vildibald.polls.model.Role
import sk.vildibald.polls.model.RoleName

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findOneByName(roleName: RoleName): Role?
}