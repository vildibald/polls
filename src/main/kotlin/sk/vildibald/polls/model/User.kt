package sk.vildibald.polls.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.NaturalId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import sk.vildibald.polls.model.audit.DateAudit
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "users", uniqueConstraints = [
    UniqueConstraint(columnNames = ["username"]),
    UniqueConstraint(columnNames = ["email"])
])
@JsonIgnoreProperties(
        value = ["created_at", "updated_at"],
        allowGetters = true
)
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
) : DateAudit()
