package sk.vildibald.polls.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import sk.vildibald.polls.model.audit.UserDateAudit
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "polls")
@JsonIgnoreProperties(
        value = ["created_at", "updated_at", "created_by", "updated_by"],
        allowGetters = true
)
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
) : UserDateAudit()

