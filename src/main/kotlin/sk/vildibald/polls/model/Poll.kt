package sk.vildibald.polls.model

import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import sk.vildibald.polls.model.audit.UserDateAuditEntity
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "polls")
data class Poll(
        @NotBlank
        @Size(max = 200)
        val question: String,

        @OneToMany(
                mappedBy = "poll",
                cascade = [CascadeType.ALL],
                fetch = FetchType.EAGER,
                orphanRemoval = true
        )
        @Size(min = 2, max = 6)
        @Fetch(FetchMode.SELECT)
        @BatchSize(size = 30)
        val choices: List<Choice> = listOf(),

        @NotNull
        val expirationDateTime: Instant
) : UserDateAuditEntity()

