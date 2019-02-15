package sk.vildibald.polls.model

import sk.vildibald.polls.model.audit.PersistableEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "choices")
data class Choice(
        @NotBlank
        @Size(max = 60)
        val text: String
): PersistableEntity() {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    lateinit var poll: Poll
}
