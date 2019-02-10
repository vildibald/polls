package sk.vildibald.polls.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sk.vildibald.polls.model.Poll

@Repository
interface PollRepository: JpaRepository<Poll, Long> {
    fun findByCreatedBy(userId: Long, pageable: Pageable): Page<Poll>

    fun countByCreatedBy(userId: Long): Long

    fun findByIdIn(pollIds: Iterable<Long>): List<Poll>

    fun findByIdIn(pollIds: Iterable<Long>, sort: Sort): List<Poll>
}