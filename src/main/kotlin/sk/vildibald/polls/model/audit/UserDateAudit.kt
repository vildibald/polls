package sk.vildibald.polls.model.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
@JsonIgnoreProperties(
        value = ["created_by", "updated_by"],
        allowGetters = true
)
abstract class UserDateAudit: DateAudit() {
    @CreatedBy
    @Column(name = "created_by")
    var createdBy: Long? = null

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: Long? = null
}