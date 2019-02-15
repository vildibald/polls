package sk.vildibald.polls.model

import org.hibernate.annotations.NaturalId
import sk.vildibald.polls.model.audit.DateAuditEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "users", uniqueConstraints = [
    UniqueConstraint(columnNames = ["username"]),
    UniqueConstraint(columnNames = ["email"])
])
data class User(
        @NotBlank
        @Size(max = 60)
        val name: String,

        @Size(max = 20)
        val username: String,

        @NaturalId
        @NotBlank
        @Size(max = 40)
        val email: String,

        @NotBlank
        @Size(max = 100)
        val password: String,

        @ManyToMany(fetch = FetchType.EAGER)
        val roles: Set<Role>
) : DateAuditEntity()
