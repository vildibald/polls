package sk.vildibald.polls.service.impl

import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import sk.vildibald.polls.MAX_PAGE_SIZE
import sk.vildibald.polls.exception.BadRequestException
import sk.vildibald.polls.exception.ResourceNotFoundException
import sk.vildibald.polls.mapping.*
import sk.vildibald.polls.model.Choice
import sk.vildibald.polls.model.Poll
import sk.vildibald.polls.model.User
import sk.vildibald.polls.payload.*
import sk.vildibald.polls.repository.PollRepository
import sk.vildibald.polls.repository.UserRepository
import sk.vildibald.polls.repository.VoteRepository
import sk.vildibald.polls.security.UserPrincipal
import sk.vildibald.polls.service.PollService
import java.time.Duration
import java.time.Instant
import org.springframework.dao.DataIntegrityViolationException
import sk.vildibald.polls.model.Vote


@Service
class PollServiceImpl(private val voteRepository: VoteRepository,
                      private val userRepository: UserRepository,
                      private val pollRepository: PollRepository)
    : PollService {

    private val logger = LoggerFactory.getLogger(PollServiceImpl::class.java)

    override fun allPolls(currentUser: UserPrincipal,
                          page: Int,
                          size: Int)
            : PagedResponse<ExtendedPollResponse> {
        validatePageNumberAndSize(page, size)
        val pageRequest = createPageRequest(page, size)
        val polls = pollRepository.findAll(pageRequest)

        if (polls.numberOfElements == 0)
            return polls.toEmptyPagedResponse()

        val pollIds = polls.map { it.id }.content
        val voteCounts = voteCountMap(pollIds)
        val pollUserVotes = pollUserVoteMap(currentUser, pollIds)
        val creators = pollCreatorMap(polls.content)

        val pollResponsesData = polls.map { poll ->
            ExtendedPollResponseData(
                    poll,
                    voteCounts,
                    creators.getValue(poll.createdBy!!),
                    pollUserVotes[poll.id]
            )
        }

        return PageConverter(ExtendedPollConverter()).convert(pollResponsesData)
    }

    override fun pollsCreatedBy(username: String,
                                currentUser: UserPrincipal,
                                page: Int,
                                size: Int)
            : PagedResponse<ExtendedPollResponse> {
        validatePageNumberAndSize(page, size)
        val user = userRepository.findOneByUsername(username)
                ?: throw ResourceNotFoundException("User", "username", username)
        val pageRequest = createPageRequest(page, size)
        val polls = pollRepository.findByCreatedBy(user.id, pageRequest)

        if (polls.numberOfElements == 0)
            return polls.toEmptyPagedResponse()

        val pollIds = polls.map { it.id }.content
        val voteCounts = voteCountMap(pollIds)
        val pollUserVotes = pollUserVoteMap(currentUser, pollIds)

        val pollResponsesData = polls.map { poll ->
            ExtendedPollResponseData(
                    poll,
                    voteCounts,
                    user,
                    pollUserVotes[poll.id]
            )
        }

        return PageConverter(ExtendedPollConverter()).convert(pollResponsesData)
    }

    override fun pollsVotedBy(username: String,
                              currentUser: UserPrincipal,
                              page: Int,
                              size: Int)
            : PagedResponse<ExtendedPollResponse> {
        validatePageNumberAndSize(page, size)
        val user = userRepository.findOneByUsername(username)
                ?: throw ResourceNotFoundException("User", "username", username)
        val pageRequest = createPageRequest(page, size)
        val pollIdsPage = voteRepository.findVotedPollIdsByUserId(user.id, pageRequest)

        if (pollIdsPage.numberOfElements == 0)
            return pollIdsPage.toEmptyPagedResponse()

        val pollIds = pollIdsPage.content
        val polls = pollRepository.findByIdIn(pollIds,
                Sort(Sort.Direction.DESC, "createdAt"))


        val voteCounts = voteCountMap(pollIds)
        val pollUserVotes = pollUserVoteMap(currentUser, pollIds)
        val creators = pollCreatorMap(polls)

        val pollConverter = ExtendedPollConverter()
        val pollResponses = polls.map { poll ->
            pollConverter.convert(ExtendedPollResponseData(
                    poll,
                    voteCounts,
                    creators.getValue(poll.createdBy!!),
                    pollUserVotes[poll.id]
            ))
        }

        return PageConverter(Converter<Long, ExtendedPollResponse> { null })
                .convert(pollIdsPage, pollResponses)
    }

    override fun createPoll(pollRequest: PollRequest,
                            currentUser: UserPrincipal)
            : PollResponse {
        val newPoll = Poll(
                question = pollRequest.question,
                choices = pollRequest.choices.map { choiceRequest -> Choice(choiceRequest.text) },
                expirationDateTime = let {
                    val now = Instant.now()
                    val pollLength = pollRequest.pollLength
                    val expiration = now +
                            Duration.ofDays(pollLength.days) +
                            Duration.ofHours(pollLength.hours)
                    expiration
                }

        )
        newPoll.choices.forEach { it.poll = newPoll }
        val savedPoll = pollRepository.save(newPoll)
        return PollConverter().convert(savedPoll)
    }

    override fun pollById(pollId: Long,
                          currentUser: UserPrincipal?)
            : ExtendedPollResponse {
        val poll = pollRepository.findById(pollId)
                .orElseThrow { ResourceNotFoundException("Poll", "id", pollId) }

        // Retrieve Vote Counts of every choice belonging to the current poll
        val votes = voteRepository.countByPollIdGroupByChoiceId(pollId)
        val choiceVotes = votes.associate { it.choiceId to it.voteCount }

        // Retrieve poll creator details
        val creator = userRepository.findById(poll.createdBy!!).orElseThrow {
            ResourceNotFoundException("Poll", "id", pollId)
        }

        // Retrieve vote done by logged in user
        return if (currentUser == null) {
            ExtendedPollConverter().convert(
                    ExtendedPollResponseData(poll, choiceVotes, creator, null)
            )
        } else {
            val userVote = voteRepository.findByUserIdAndPollId(currentUser.id, pollId)
            ExtendedPollConverter().convert(
                    ExtendedPollResponseData(poll, choiceVotes, creator, userVote?.choice?.id)
            )
        }
    }

    override fun castVote(pollId: Long,
                          voteRequest: VoteRequest,
                          currentUser: UserPrincipal)
            : ExtendedPollResponse {
        val poll = pollRepository.findById(pollId)
                .orElseThrow { ResourceNotFoundException("Poll", "id", pollId) }

        if (poll.expirationDateTime.isBefore(Instant.now())) {
            throw BadRequestException("Sorry! This Poll has already expired")
        }

        val user = userRepository.getOne(currentUser.id)

        val selectedChoice = poll.choices.firstOrNull { choice -> choice.id == voteRequest.choiceId }
                ?: throw ResourceNotFoundException("Choice", "id", voteRequest.choiceId)

        val newVote = Vote(poll = poll, user = user, choice = selectedChoice)

        val savedVote = try {
            voteRepository.save(newVote)
        } catch (ex: DataIntegrityViolationException) {
            logger.info("User {} has already voted in Poll {}", currentUser.id, pollId)
            throw BadRequestException("Sorry! You have already cast your vote in this poll")
        }

        //-- Vote Saved, Return the updated Poll Response now --

        // Retrieve Vote Counts of every choice belonging to the current poll
        val votes = voteRepository.countByPollIdGroupByChoiceId(pollId)

        val choiceVotes = votes.associate { it.choiceId to it.voteCount }

        // Retrieve poll creator details
        val creator = userRepository.findById(poll.createdBy!!)
                .orElseThrow { ResourceNotFoundException("User", "id", poll.createdBy!!) }

        return ExtendedPollConverter().convert(
                ExtendedPollResponseData(poll, choiceVotes, creator, savedVote.choice.id)
        )
    }

    private fun validatePageNumberAndSize(page: Int, size: Int) {
        if (page < 0) {
            throw BadRequestException("Page number cannot be less than zero.")
        }

        if (size > MAX_PAGE_SIZE) {
            throw BadRequestException("Page size must not be greater than $MAX_PAGE_SIZE")
        }
    }

    private fun voteCountMap(pollIds: Iterable<Long>): Map<Long, Long> =
            voteRepository.countByPollIdInGroupByChoiceId(pollIds)
                    .associate { it.choiceId to it.voteCount }

    private fun pollUserVoteMap(currentUser: UserPrincipal?,
                                pollIds: Iterable<Long>)
            : Map<Long, Long> =
            if (currentUser == null) emptyMap()
            else voteRepository.findByUserIdAndPollIdIn(currentUser.id, pollIds)
                    .associateBy({ it.poll.id }, { it.choice.id })

    private fun pollCreatorMap(polls: Iterable<Poll>): Map<Long, User> =
            userRepository.findByIdIn(
                    polls.map { it.createdBy!! }.distinct()
            ).associateBy { it.id }

    private fun createPageRequest(page: Int, size: Int): Pageable =
            PageRequest.of(page, size, Sort.Direction.DESC, "createdAt")

}