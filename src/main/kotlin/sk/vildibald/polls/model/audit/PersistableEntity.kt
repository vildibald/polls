package sk.vildibald.polls.model.audit

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class PersistableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int


}