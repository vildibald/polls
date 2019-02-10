package sk.vildibald.polls.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import sk.vildibald.polls.model.Vote
import sk.vildibald.polls.model.ChoiceVoteCount

@Repository
interface VoteRepository : JpaRepository<Vote, Long> {
    @Query(value = "SELECT NEW sk.vildibald.polls.model.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id in :pollIds GROUP BY v.choice.id")
    fun countByPollIdInGroupByChoiceId(@Param("pollIds") pollIds: Iterable<Long>)
            : List<ChoiceVoteCount>

    @Query(value = "SELECT NEW sk.vildibald.polls.model.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id = :pollId GROUP BY v.choice.id")
    fun countByPollIdGroupByChoiceId(@Param("pollId") pollId: Long): List<ChoiceVoteCount>

    @Query(value = "SELECT v FROM Vote v where v.user.id = :userId and v.poll.id in :pollIds")
    fun findByUserIdAndPollIdIn(@Param("userId") userId: Long,
                                @Param("pollIds") pollIds: Iterable<Long>): List<Vote>

    @Query(value = "SELECT v FROM Vote v where v.user.id = :userId and v.poll.id = :pollId")
    fun findByUserIdAndPollId(@Param("userId") userId: Long,
                              @Param("pollId") pollId: Long?): Vote?

    @Query(value = "SELECT COUNT(v.id) from Vote v where v.user.id = :userId")
    fun countByUserId(@Param("userId") userId: Long): Long

    @Query(value = "SELECT v.poll.id FROM Vote v WHERE v.user.id = :userId")
    fun findVotedPollIdsByUserId(@Param("userId") userId: Long,
                                 pageable: Pageable): Page<Long>
}