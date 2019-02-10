package sk.vildibald.polls.model

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Enumerated(EnumType.STRING)
        @NaturalId
        @Column(length = 60)
        val name: RoleName
)