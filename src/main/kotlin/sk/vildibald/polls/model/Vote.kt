package sk.vildibald.polls.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.vildibald.polls.model.audit.DateAuditEntity
import javax.persistence.*


@Entity
@Table(name = "votes", uniqueConstraints = [
    UniqueConstraint(columnNames = [
        "poll_id",
        "user_id"
    ])
])
@JsonIgnoreProperties(
        value = ["created_at", "updated_at"],
        allowGetters = true
)
data class Vote(
        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "poll_id", nullable = false)
        val poll: Poll,

        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "choice_id", nullable = false)
        val choice: Choice,

        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "user_id", nullable = false)
        val user: User

) : DateAuditEntity()