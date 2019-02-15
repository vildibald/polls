package sk.vildibald.polls.model.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.Instant
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(
        value = ["created_at", "updated_at"],
        allowGetters = true
)
abstract class DateAuditEntity : PersistableEntity(), Serializable {
    @CreatedDate
    @Column(name = "created_at")
    lateinit var createdAt: Instant

    @LastModifiedDate
    @Column(name = "updated_at")
    lateinit var updatedAt: Instant
}